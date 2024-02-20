package pw.vintr.music.ui.feature.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.user.useCase.RegisterUseCase
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(RegisterScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeLogin(value: String) {
        _screenState.update { it.copy(login = value) }
    }

    fun changePassword(value: String) {
        _screenState.update { it.copy(password = value) }
    }

    fun changeRepeatPassword(value: String) {
        _screenState.update { it.copy(repeatPassword = value) }
    }

    fun changeFirstName(value: String) {
        _screenState.update { it.copy(firstName = value) }
    }

    fun changeLastName(value: String) {
        _screenState.update { it.copy(lastName = value) }
    }

    fun register() {
        launch(createExceptionHandler {
            _screenState.update { it.copy(isRegistering = false) }
        }) {
            _screenState.update { it.copy(isRegistering = true) }

            registerUseCase.invoke(
                login = _screenState.value.login,
                password = _screenState.value.password,
                firstName = _screenState.value.firstName,
                lastName = _screenState.value.lastName,
            )

            _screenState.update { it.copy(isRegistering = false) }
            openSelectServer()
        }
    }

    private fun openSelectServer() {
        navigator.replaceAll(Screen.SelectServer())
    }
}

data class RegisterScreenState(
    val login: String = String.Empty,
    val password: String = String.Empty,
    val repeatPassword: String = String.Empty,
    val firstName: String = String.Empty,
    val lastName: String = String.Empty,
    val isRegistering: Boolean = false,
) {
    val formIsValid = login.isNotBlank() &&
            password.isNotBlank() &&
            password == repeatPassword &&
            firstName.isNotEmpty() &&
            lastName.isNotEmpty()
}
