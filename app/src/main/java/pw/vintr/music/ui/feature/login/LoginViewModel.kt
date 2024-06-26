package pw.vintr.music.ui.feature.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.alert.interactor.AlertInteractor
import pw.vintr.music.domain.alert.model.AlertModel
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class LoginViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    private val alertInteractor: AlertInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(LoginScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeLogin(value: String) {
        _screenState.update {
            if ((value.length - it.login.length) > 1) {
                it.copy(login = value.trim())
            } else {
                it.copy(login = value)
            }
        }
    }

    fun changePassword(value: String) {
        _screenState.update { it.copy(password = value) }
    }

    fun authorize() {
        launch(createExceptionHandler {
            _screenState.update { it.copy(isAuthorizing = false) }
            alertInteractor.showAlert(AlertModel.LoginError())
        }) {
            _screenState.update { it.copy(isAuthorizing = true) }

            authorizeUseCase.invoke(
                login = _screenState.value.login.trim(),
                password = _screenState.value.password.trim(),
            )

            _screenState.update { it.copy(isAuthorizing = false) }
            openSelectServer()
        }
    }

    fun openRegister() {
        navigator.forward(Screen.Register)
    }

    private fun openSelectServer() {
        navigator.replaceAll(Screen.SelectServer())
    }
}

data class LoginScreenState(
    val login: String = String.Empty,
    val password: String = String.Empty,
    val isAuthorizing: Boolean = false,
) {
    val formIsValid = login.isNotBlank() && password.isNotBlank()
}
