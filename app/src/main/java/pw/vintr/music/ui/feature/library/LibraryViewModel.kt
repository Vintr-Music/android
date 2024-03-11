package pw.vintr.music.ui.feature.library

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.useCase.GetArtistListUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class LibraryViewModel(
    private val getArtistListUseCase: GetArtistListUseCase,
) : BaseViewModel() {

    companion object {
        private const val MAX_ARTISTS_DISPLAY = 9
    }

    private val _screenState = MutableStateFlow<BaseScreenState<LibraryScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling { loadScreenData() }
    }

    fun refreshData() {
        _screenState.refreshWithStateHandling { loadScreenData() }
    }

    private suspend fun loadScreenData() = LibraryScreenData(
        artists = getArtistListUseCase
            .invoke()
            .shuffled()
            .take(MAX_ARTISTS_DISPLAY)
    )

    fun openFavoriteArtists() {
        navigator.forward(Screen.ArtistFavoriteList)
    }

    fun openFavoriteAlbums() {
        navigator.forward(Screen.AlbumFavoriteList)
    }

    fun openPlaylists() {
        navigator.forward(Screen.PlaylistList)
    }

    fun openAllArtists() {
        navigator.forward(Screen.ArtistList)
    }

    fun openArtist(artist: ArtistModel) {
        navigator.forward(Screen.ArtistDetails(artist))
    }
}

data class LibraryScreenData(
    val personalLibraryItems: List<PersonalLibraryItem> = listOf(
        PersonalLibraryItem.ARTISTS,
        PersonalLibraryItem.ALBUMS,
        PersonalLibraryItem.PLAYLISTS,
    ),
    val artists: List<ArtistModel>,
)

enum class PersonalLibraryItem(val iconRes: Int, val titleRes: Int) {
    ARTISTS(iconRes = R.drawable.ic_artist, R.string.library_artists),
    ALBUMS(iconRes = R.drawable.ic_album, R.string.library_albums),
    PLAYLISTS(iconRes = R.drawable.ic_playlist, R.string.library_playlists)
}
