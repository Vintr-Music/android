package pw.vintr.music.ui.feature.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel

class LoginViewModel(
    private val authorizeUseCase: AuthorizeUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(LoginScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeEmail(value: String) {
        _screenState.update { it.copy(email = value) }
    }

    fun changePassword(value: String) {
        _screenState.update { it.copy(password = value) }
    }

    fun authorize() {
        launch(createExceptionHandler {
            _screenState.update { it.copy(isAuthorizing = false) }
        }) {
            _screenState.update { it.copy(isAuthorizing = true) }

            authorizeUseCase.invoke(
                email = _screenState.value.email,
                password = _screenState.value.password,
            )

            _screenState.update { it.copy(isAuthorizing = false) }
        }
    }
}

data class LoginScreenState(
    val email: String = String.Empty,
    val password: String = String.Empty,
    val isAuthorizing: Boolean = false,
) {
    val formIsValid = email.isNotBlank() && password.isNotBlank()
}
