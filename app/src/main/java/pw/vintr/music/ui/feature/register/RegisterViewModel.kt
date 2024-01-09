package pw.vintr.music.ui.feature.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(RegisterScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeEmail(value: String) {
        _screenState.update { it.copy(email = value) }
    }

    fun changePassword(value: String) {
        _screenState.update { it.copy(password = value) }
    }

    fun changeRepeatPassword(value: String) {
        _screenState.update { it.copy(repeatPassword = value) }
    }

    fun register() {
        // TODO: register
    }
}

data class RegisterScreenState(
    val email: String = String.Empty,
    val password: String = String.Empty,
    val repeatPassword: String = String.Empty,
    val isRegistering: Boolean = false,
) {
    val formIsValid = email.isNotBlank() && password.isNotBlank() && password == repeatPassword
}
