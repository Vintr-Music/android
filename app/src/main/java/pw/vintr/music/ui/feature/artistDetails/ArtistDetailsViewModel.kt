package pw.vintr.music.ui.feature.artistDetails

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ArtistDetailsViewModel(
    private val artist: ArtistModel,
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<ArtistDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            ArtistDetailsScreenData(
                artist = artist,
                albums = getArtistAlbumsUseCase.invoke(artist.name)
            )
        }
    }

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }
}

data class ArtistDetailsScreenData(
    val artist: ArtistModel,
    val albums: List<AlbumModel>,
) {
    val title = artist.name
}
