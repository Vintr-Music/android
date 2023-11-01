package pw.vintr.music.ui.feature.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.mainPage.model.MainPageItemModel
import pw.vintr.music.domain.mainPage.model.MainPageWelcomeModel
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

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
        launch(createExceptionHandler {
            _screenState.value = BaseScreenState.Error()
        }) {
            _screenState.value = BaseScreenState.Loading()
            _screenState.value = BaseScreenState.Loaded(
                HomeScreenData(
                    welcome = MainPageWelcomeModel.getByNowTime(),
                    items = getMainPageContentUseCase.invoke(),
                )
            )
        }
    }
}

data class HomeScreenData(
    val welcome: MainPageWelcomeModel,
    val items: List<MainPageItemModel>,
)
