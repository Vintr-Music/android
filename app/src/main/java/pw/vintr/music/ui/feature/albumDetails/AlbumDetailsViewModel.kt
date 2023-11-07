package pw.vintr.music.ui.feature.albumDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

class AlbumDetailsViewModel(
    val album: AlbumModel,
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<AlbumDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _screenState.loadWithStateHandling {
            AlbumDetailsScreenData(
                album = album,
                tracks = getAlbumTracksUseCase.invoke(
                    artist = album.artist,
                    album = album.name
                )
            )
        }
    }
}

data class AlbumDetailsScreenData(
    val album: AlbumModel,
    val tracks: List<TrackModel>,
) {
    val title = album.name

    val subtitle = listOf(album.artist, album.year?.toString().orEmpty())
        .filter { it.isNotEmpty() }
        .joinToString(separator = String.Comma + String.Space)
}
