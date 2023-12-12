package pw.vintr.music.ui.feature.menu

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.useCase.GetSelectedServerUseCase
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class MenuViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSelectedServerUseCase: GetSelectedServerUseCase
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
        navigator.forward(
            Screen.SelectServer(usePrimaryMountToolbar = false),
            type = NavigatorType.Root
        )
    }

    fun openSettings() {
        navigator.forward(Screen.Settings)
    }
}

data class MenuScreenData(
    val user: UserModel,
    val server: ServerModel
)
