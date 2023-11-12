package pw.vintr.music.ui.feature.albumDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.PlayerSessionModel
import pw.vintr.music.domain.player.model.PlayerStatusModel
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

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

        if (
            it.session is PlayerSessionModel.Album &&
            it.session.album == album
        ) {
            if (isPlaying) {
                AlbumPlayingState.Playing(it.currentTrack)
            } else {
                AlbumPlayingState.Paused(it.currentTrack)
            }
        } else {
            AlbumPlayingState.Idle
        }
    }.stateInThis(AlbumPlayingState.Idle)

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
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

    object Idle : AlbumPlayingState {

        override val playingTrack: TrackModel? = null
    }

    data class Paused(override val playingTrack: TrackModel?) : AlbumPlayingState

    data class Playing(override val playingTrack: TrackModel?) : AlbumPlayingState
}
