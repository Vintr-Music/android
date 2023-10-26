package pw.vintr.music.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pw.vintr.music.tools.extension.popUpToTop

private const val NAVIGATION_EFFECT_KEY = "navigation"

class Navigator {

    private val _actionFlow = MutableSharedFlow<NavigatorAction>(extraBufferCapacity = 1)

    private var currentNavigatorType: NavigatorType = NavigatorType.Root

    val actionFlow = _actionFlow.asSharedFlow()

    fun back() {
        _actionFlow.tryEmit(
            NavigatorAction.Back(
                navigatorType = currentNavigatorType
            )
        )
    }

    fun forward(screen: Screen) {
        _actionFlow.tryEmit(
            NavigatorAction.Forward(
                screen = screen,
                navigatorType = currentNavigatorType
            )
        )
    }

    fun replaceAll(screen: Screen) {
        _actionFlow.tryEmit(
            NavigatorAction.ReplaceAll(
                screen = screen,
                navigatorType = currentNavigatorType
            )
        )
    }
}

sealed class NavigatorAction {

    abstract val navigatorType: NavigatorType

    data class Back(
        override val navigatorType: NavigatorType
    ) : NavigatorAction()

    data class Forward(
        val screen: Screen,
        override val navigatorType: NavigatorType
    ) : NavigatorAction()

    data class ReplaceAll(
        val screen: Screen,
        override val navigatorType: NavigatorType
    ) : NavigatorAction()
}

interface NavigatorType {
    object Root : NavigatorType
}

@Composable
fun NavigatorEffect(
    type: NavigatorType,
    navigator: Navigator,
    controller: NavController
) {
    LaunchedEffect(NAVIGATION_EFFECT_KEY) {
        navigator.actionFlow.onEach { action ->
            if (action.navigatorType == type) {
                when (action) {
                    is NavigatorAction.Back -> {
                        controller.navigateUp()
                    }
                    is NavigatorAction.Forward -> {
                        controller.navigate(action.screen.route)
                    }
                    is NavigatorAction.ReplaceAll -> {
                        controller.navigate(
                            action.screen.route
                        ) { popUpToTop(controller) }
                    }
                }
            }
        }.launchIn(scope = this)
    }
}
