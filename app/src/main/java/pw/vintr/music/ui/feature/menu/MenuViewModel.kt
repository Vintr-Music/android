package pw.vintr.music.ui.feature.menu

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class MenuViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<MenuState>(MenuState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        launch(createExceptionHandler {
            _screenState.value = MenuState.Error
        }) {
            _screenState.value = MenuState.Loading

            val user = async { getProfileUseCase.invoke() }
            // TODO: load other data

            _screenState.value = MenuState.Loaded(
                user = user.await(),
            )
        }
    }

    fun openSettings() {
        navigator.forward(Screen.Settings)
    }
}

sealed interface MenuState {
    object Loading : MenuState

    object Error : MenuState

    data class Loaded(
        val user: UserModel
    ) : MenuState
}
