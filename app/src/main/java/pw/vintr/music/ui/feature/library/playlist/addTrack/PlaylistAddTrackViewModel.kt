package pw.vintr.music.ui.feature.library.playlist.addTrack

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import pw.vintr.music.domain.alert.interactor.AlertInteractor
import pw.vintr.music.domain.alert.model.AlertModel
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.tools.extension.addItem
import pw.vintr.music.tools.extension.removeItem
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.base.mapToScreenState
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class PlaylistAddTrackViewModel(
    private val trackId: String,
    private val playlistInteractor: PlaylistInteractor,
    private val alertInteractor: AlertInteractor,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _playlistsState = playlistInteractor.dataFlow
        .mapLatest(::mapToScreenState)

    private val _playlistsWithTrackState =
        MutableStateFlow<BaseScreenState<List<PlaylistModel>>>(BaseScreenState.Loading())

    private val _playlistsWithLoadingState =
        MutableStateFlow<List<PlaylistModel>>(listOf())

    val screenState = combine(
        _playlistsState,
        _playlistsWithTrackState,
        _playlistsWithLoadingState
    ) { playlists, withTrack, withInteraction ->
        when {
            playlists is BaseScreenState.Error || withTrack is BaseScreenState.Error -> {
                BaseScreenState.Error()
            }
            playlists is BaseScreenState.Empty -> {
                BaseScreenState.Empty()
            }
            playlists is BaseScreenState.Loaded && withTrack is BaseScreenState.Loaded -> {
                BaseScreenState.Loaded(
                    data = playlists.data.map { playlist ->
                        PlaylistInteractionItem(
                            playlist = playlist,
                            containsTargetTrack = withTrack.data.contains(playlist),
                            isLoading = withInteraction.contains(playlist)
                        )
                    },
                    isRefreshing = playlists.isRefreshing || withTrack.isRefreshing
                )
            }
            else -> {
                BaseScreenState.Loading()
            }
        }
    }.stateInThis(BaseScreenState.Loading())

    init {
        loadData()
    }

    fun loadData() {
        // Load all playlists
        launch { playlistInteractor.loadPlaylists() }

        // Load playlists with target track
        _playlistsWithTrackState.loadWithStateHandling {
            playlistInteractor.getPlaylistsWithTrack(trackId)
        }
    }

    fun addTrackToPlaylist(playlist: PlaylistModel) {
        if (_playlistsWithLoadingState.value.contains(playlist)) {
            return
        }

        launch(createExceptionHandler {
            alertInteractor.showAlert(AlertModel.CommonError())
        }) {
            withLoading(
                setLoadingCallback = { isLoading ->
                    if (isLoading) {
                        _playlistsWithLoadingState.addItem(playlist)
                    } else {
                        _playlistsWithLoadingState.removeItem(playlist)
                    }
                },
                action = {
                    playlistInteractor.addTrackToPlaylist(
                        playlistId = playlist.id,
                        trackId = trackId
                    )
                    _playlistsWithTrackState.updateLoaded { items ->
                        items
                            .toMutableList()
                            .apply { add(playlist) }
                    }
                }
            )
        }
    }

    fun openCreatePlaylist() {
        navigator.forward(
            screen = Screen.PlaylistCreate,
            type = NavigatorType.Root
        )
    }
}

data class PlaylistInteractionItem(
    val playlist: PlaylistModel,
    val containsTargetTrack: Boolean,
    val isLoading: Boolean
)
