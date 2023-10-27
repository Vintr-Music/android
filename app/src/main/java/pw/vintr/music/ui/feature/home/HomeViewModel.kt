package pw.vintr.music.ui.feature.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.mainPage.model.MainPageItemModel
import pw.vintr.music.domain.mainPage.model.MainPageWelcomeModel
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.ui.base.BaseViewModel

class HomeViewModel(
    private val getMainPageContentUseCase: GetMainPageContentUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<HomeState>(HomeState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        launch(createExceptionHandler {
            _screenState.value = HomeState.Error
        }) {
            _screenState.value = HomeState.Loading
            _screenState.value = HomeState.Loaded(
                welcome = MainPageWelcomeModel.getByNowTime(),
                items = getMainPageContentUseCase.invoke(),
            )
        }
    }
}

sealed interface HomeState {
    object Loading : HomeState

    object Error : HomeState

    data class Loaded(
        val welcome: MainPageWelcomeModel,
        val items: List<MainPageItemModel>,
    ) : HomeState
}
