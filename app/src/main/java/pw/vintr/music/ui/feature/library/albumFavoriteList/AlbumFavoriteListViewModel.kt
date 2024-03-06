package pw.vintr.music.ui.feature.library.albumFavoriteList

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import pw.vintr.music.domain.base.BaseDomainState
import pw.vintr.music.domain.favorite.FavoriteAlbumsInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class AlbumFavoriteListViewModel(
    private val favoriteAlbumsInteractor: FavoriteAlbumsInteractor
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val screenState = favoriteAlbumsInteractor.dataFlow.mapLatest { domainState ->
        when (domainState) {
            is BaseDomainState.Error -> {
                BaseScreenState.Error()
            }
            is BaseDomainState.Loading -> {
                BaseScreenState.Loading()
            }
            is BaseDomainState.Loaded -> {
                BaseScreenState.Loaded(
                    data = domainState.data,
                    isRefreshing = domainState.isRefreshing
                )
            }
        }
    }.stateInThis(initialValue = BaseScreenState.Loading())

    init {
        loadData()
    }

    fun loadData() {
        launch { favoriteAlbumsInteractor.loadData() }
    }

    fun refreshData() {
        launch { favoriteAlbumsInteractor.refreshData() }
    }

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }
}
