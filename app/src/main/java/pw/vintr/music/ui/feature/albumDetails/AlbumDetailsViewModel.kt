package pw.vintr.music.ui.feature.albumDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.PlayerState
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

    val albumPlayingState = combine(
        playerInteractor.playerState,
        _screenState
    ) { playerState, screenState ->
        if (screenState is BaseScreenState.Loaded) {
            val tracks = screenState.data.tracks
            val track = tracks.find { playerState.trackId == it.md5 }

            if (track != null && playerState is PlayerState.Playing) {
                AlbumPlayingState.Playing(track)
            } else {
                AlbumPlayingState.Idle
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
        _screenState.withLoaded { playerInteractor.invokePlay(it.tracks, startIndex) }
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
    object Idle

    data class Playing(val track: TrackModel)
}
