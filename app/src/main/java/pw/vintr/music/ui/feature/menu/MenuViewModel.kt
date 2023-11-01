package pw.vintr.music.ui.feature.menu

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class MenuViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<MenuScreenData>>(
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

            val user = async { getProfileUseCase.invoke() }
            // TODO: load other data

            _screenState.value = BaseScreenState.Loaded(
                MenuScreenData(
                    user = user.await()
                ),
            )
        }
    }

    fun openSettings() {
        navigator.forward(Screen.Settings)
    }
}

data class MenuScreenData(
    val user: UserModel
)
