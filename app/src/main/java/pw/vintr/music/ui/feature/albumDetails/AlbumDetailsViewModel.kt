package pw.vintr.music.ui.feature.albumDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.trackDetails.entity.TrackDetailsOption
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class AlbumDetailsViewModel(
    private val album: AlbumModel,
    private val playerInteractor: PlayerInteractor,
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<AlbumDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val albumPlayingState = playerInteractor.playerState.map {
        val isPlaying = it.status == PlayerStatusModel.PLAYING
        val isLoading = it.status == PlayerStatusModel.LOADING

        if (
            it.session is PlayerSessionModel.Album &&
            it.session.album == album
        ) {
            when {
                isLoading -> AlbumPlayingState.Loading(it.currentTrack)
                isPlaying -> AlbumPlayingState.Playing(it.currentTrack)
                else -> AlbumPlayingState.Paused(it.currentTrack)
            }
        } else {
            AlbumPlayingState.Idle(it.currentTrack)
        }
    }.stateInThis(AlbumPlayingState.Idle())

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            AlbumDetailsScreenData(
                album = album,
                tracks = getAlbumTracksUseCase.invoke(
                    artist = album.artist.name,
                    album = album.name
                )
            )
        }
    }

    fun playAlbum(startIndex: Int = 0) {
        launch {
            _screenState.withLoaded {
                playerInteractor.playAlbum(it.tracks, album, startIndex)
            }
        }
    }

    fun pauseAlbum() {
        playerInteractor.pause()
    }

    fun resumeAlbum() {
        playerInteractor.resume()
    }

    fun openTrackDetails(track: TrackModel) {
        navigator.forward(
            Screen.TrackDetails(
                trackModel = track,
                allowedOptions = TrackDetailsOption.optionsExceptAlbumNavigate
            ),
            NavigatorType.Root
        )
    }
}

data class AlbumDetailsScreenData(
    val album: AlbumModel,
    val tracks: List<TrackModel>,
) {
    val title = album.name

    val subtitle = listOf(album.artist.name, album.year?.toString().orEmpty())
        .filter { it.isNotEmpty() }
        .joinToString(separator = String.Comma + String.Space)
}

sealed interface AlbumPlayingState {

    val playingTrack: TrackModel?

    data class Idle(override val playingTrack: TrackModel? = null) : AlbumPlayingState

    data class Paused(override val playingTrack: TrackModel?) : AlbumPlayingState

    data class Playing(override val playingTrack: TrackModel?) : AlbumPlayingState

    data class Loading(override val playingTrack: TrackModel?) : AlbumPlayingState
}
