package pw.vintr.music.ui.feature.artistDetails

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import pw.vintr.music.domain.favorite.FavoriteArtistsInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ArtistDetailsViewModel(
    private val artist: ArtistModel,
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
    private val favoriteArtistsInteractor: FavoriteArtistsInteractor
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<ArtistDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    private var favoritesInteractionJob: Job? = null

    init {
        loadData()
        subscribeFavoriteEvents()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val albums = async {
                getArtistAlbumsUseCase.invoke(artist.name)
            }
            val isFavorite = async {
                favoriteArtistsInteractor.isInFavorites(artist)
            }

            ArtistDetailsScreenData(
                artist = artist,
                albums = albums.await(),
                isFavorite = isFavorite.await()
            )
        }
    }

    private fun subscribeFavoriteEvents() {
        launch {
            favoriteArtistsInteractor.events
                .filter { it.artist == artist }
                .collectLatest { event ->
                    when (event) {
                        is FavoriteArtistsInteractor.Event.Added -> {
                            _screenState.updateLoaded { it.copy(isFavorite = true) }
                        }

                        is FavoriteArtistsInteractor.Event.Removed -> {
                            _screenState.updateLoaded { it.copy(isFavorite = false) }
                        }
                    }
                }
        }
    }

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }

    fun addToFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteArtistsInteractor.addToFavorites(artist)
            }
        }
    }

    fun removeFromFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteArtistsInteractor.removeFromFavorites(artist)
            }
        }
    }
}

data class ArtistDetailsScreenData(
    val artist: ArtistModel,
    val albums: List<AlbumModel>,
    val isFavorite: Boolean,
) {
    val title = artist.name
}
