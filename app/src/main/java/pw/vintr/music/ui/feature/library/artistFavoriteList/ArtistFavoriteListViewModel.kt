package pw.vintr.music.ui.feature.library.artistFavoriteList

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import pw.vintr.music.domain.base.BaseDomainState
import pw.vintr.music.domain.favorite.FavoriteArtistsInteractor
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ArtistFavoriteListViewModel(
    private val favoriteArtistsInteractor: FavoriteArtistsInteractor
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val screenState = favoriteArtistsInteractor.dataFlow.mapLatest { domainState ->
        when (domainState) {
            is BaseDomainState.Error -> {
                BaseScreenState.Error()
            }
            is BaseDomainState.Loading -> {
                BaseScreenState.Loading()
            }
            is BaseDomainState.Loaded -> {
                if (domainState.data.isNotEmpty()) {
                    BaseScreenState.Loaded(
                        data = domainState.data,
                        isRefreshing = domainState.isRefreshing
                    )
                } else {
                    BaseScreenState.Empty()
                }
            }
        }
    }.stateInThis(initialValue = BaseScreenState.Loading())

    init {
        loadData()
    }

    fun loadData() {
        launch { favoriteArtistsInteractor.loadData() }
    }

    fun refreshData() {
        launch { favoriteArtistsInteractor.refreshData() }
    }

    fun onArtistClick(artist: ArtistModel) {
        navigator.forward(Screen.ArtistDetails(artist))
    }
}
