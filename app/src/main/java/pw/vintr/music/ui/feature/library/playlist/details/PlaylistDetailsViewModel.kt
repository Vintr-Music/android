package pw.vintr.music.ui.feature.library.playlist.details

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.domain.playlist.model.PlaylistRecordModel
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class PlaylistDetailsViewModel(
    private val playlistId: String,
    private val playerInteractor: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<PlaylistDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val playlistPlayingState = playerInteractor.playerState.map {
        if (
            it.session is PlayerSessionModel.Playlist &&
            it.session.playlistId == playlistId
        ) {
            PlaylistPlayingState(
                playingTrack = it.currentTrack,
                playerStatus = it.status
            )
        } else {
            PlaylistPlayingState(it.currentTrack)
        }
    }.stateInThis(PlaylistPlayingState())

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
        subscribePlaylistEvents()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val tracks = async {
                playlistInteractor.getPlaylistTracks(playlistId)
            }
            val playlist = async {
                playlistInteractor.getPlaylistById(playlistId)
            }

            PlaylistDetailsScreenData(
                playlist = playlist.await(),
                tracks = tracks.await(),
            )
        }
    }

    private fun subscribePlaylistEvents() {
        launch {
            playlistInteractor.events
                .filter { it.playlistId == playlistId }
                .collectLatest(::processPlaylistEvent)
        }
    }

    private fun processPlaylistEvent(event: PlaylistInteractor.Event) {
        _screenState.updateLoaded { freezeState ->
            when (event) {
                is PlaylistInteractor.Event.AddedTrack -> {
                    freezeState.copy(
                        tracks = listOf(event.record, *freezeState.tracks.toTypedArray())
                    )
                }
                is PlaylistInteractor.Event.RemovedTrack -> {
                    freezeState.copy(
                        tracks = freezeState.tracks.filter { it != event.record }
                    )
                }
                is PlaylistInteractor.Event.UpdatedTracks -> {
                    freezeState.copy(tracks = event.records)
                }
            }
        }
    }

    fun playPlaylist(startIndex: Int = 0) {
        launch {
            _screenState.withLoaded { data ->
                playerInteractor.playPlaylist(
                    tracks = data.tracks.map { it.track },
                    playlist = data.playlist,
                    startIndex = startIndex
                )
            }
        }
    }

    fun pausePlaylist() {
        playerInteractor.pause()
    }

    fun resumePlaylist() {
        playerInteractor.resume()
    }

    fun openTrackAction(track: TrackModel) {
        navigator.forward(
            Screen.TrackActionSheet(
                trackModel = track,
                allowedActions = listOf(
                    TrackAction.GO_TO_ALBUM,
                    TrackAction.GO_TO_ARTIST,
                    TrackAction.PLAY_NEXT,
                    TrackAction.ADD_TO_QUEUE,
                )
            ),
            NavigatorType.Root
        )
    }

    fun openPlaylistAction() {
        // TODO: open
    }
}

data class PlaylistDetailsScreenData(
    val playlist: PlaylistModel,
    val tracks: List<PlaylistRecordModel>,
) {
    val title = playlist.name

    val subtitle = playlist.description
}

data class PlaylistPlayingState(
    val playingTrack: TrackModel? = null,
    val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE
)
