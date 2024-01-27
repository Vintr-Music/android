package pw.vintr.music.ui.feature.menu

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.useCase.GetSelectedServerUseCase
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.domain.user.useCase.LogoutUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.menu.logout.LogoutConfirmResult
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class MenuViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSelectedServerUseCase: GetSelectedServerUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<MenuScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val user = async { getProfileUseCase.invoke() }
            val server = async { getSelectedServerUseCase.invoke() }

            MenuScreenData(
                user = user.await(),
                server = server.await()
            )
        }
    }

    fun openServerSelection() {
        navigator.switchNavigatorType(NavigatorType.Root)
        navigator.forward(Screen.SelectServer(usePrimaryMountToolbar = false))
    }

    fun openSettings() {
        navigator.forward(Screen.Settings)
    }

    fun openLogoutDialog() {
        handleResult(LogoutConfirmResult.KEY) {
            navigator.forwardWithResult<LogoutConfirmResult>(
                screen = Screen.LogoutConfirmDialog,
                type = NavigatorType.Root,
                resultKey = LogoutConfirmResult.KEY,
            ) { logout() }
        }
    }

    private fun logout() {
        launch(Dispatchers.Main) {
            logoutUseCase.invoke()
            playerInteractor.destroySession()

            navigator.replaceAll(
                screen = Screen.Login,
                type = NavigatorType.Root,
                applyNavigatorType = true
            )
        }
    }

    fun openAbout() {
        // TODO: open about
    }
}

data class MenuScreenData(
    val user: UserModel,
    val server: ServerModel
)
