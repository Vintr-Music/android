package pw.vintr.music.ui.feature.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.mainPage.model.MainPageItemModel
import pw.vintr.music.domain.mainPage.model.MainPageWelcomeModel
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class HomeViewModel(
    private val getMainPageContentUseCase: GetMainPageContentUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<HomeScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _screenState.loadWithStateHandling {
            HomeScreenData(
                welcome = MainPageWelcomeModel.getByNowTime(),
                items = getMainPageContentUseCase.invoke(),
            )
        }
    }

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails, Screen.AlbumDetails.arguments(album))
    }
}

data class HomeScreenData(
    val welcome: MainPageWelcomeModel,
    val items: List<MainPageItemModel>,
)
