package pw.vintr.music.ui.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
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

    fun switchNavigatorType(type: NavigatorType) {
        currentNavigatorType = type
    }

    fun back(type: NavigatorType? = null) {
        _actionFlow.tryEmit(
            NavigatorAction.Back(
                navigatorType = type ?: currentNavigatorType
            )
        )
    }

    fun backToStart(type: NavigatorType? = null) {
        _actionFlow.tryEmit(
            NavigatorAction.BackToStart(
                navigatorType = type ?: currentNavigatorType
            )
        )
    }

    fun forward(
        screen: Screen,
        arguments: Bundle? = null,
        type: NavigatorType? = null
    ) {
        _actionFlow.tryEmit(
            NavigatorAction.Forward(
                screen = screen,
                navigatorType = type ?: currentNavigatorType,
                arguments = arguments,
            )
        )
    }

    fun replaceAll(
        screen: Screen,
        arguments: Bundle? = null,
        type: NavigatorType? = null
    ) {
        _actionFlow.tryEmit(
            NavigatorAction.ReplaceAll(
                screen = screen,
                navigatorType = type ?: currentNavigatorType,
                arguments = arguments,
            )
        )
    }
}

sealed class NavigatorAction {

    abstract val navigatorType: NavigatorType

    open val arguments: Bundle? = null

    data class Back(
        override val navigatorType: NavigatorType
    ) : NavigatorAction()

    data class BackToStart(
        override val navigatorType: NavigatorType
    ) : NavigatorAction()

    data class Forward(
        val screen: Screen,
        override val navigatorType: NavigatorType,
        override val arguments: Bundle? = null,
    ) : NavigatorAction()

    data class ReplaceAll(
        val screen: Screen,
        override val navigatorType: NavigatorType,
        override val arguments: Bundle? = null,
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
                    is NavigatorAction.BackToStart -> {
                        controller.popBackStack(
                            destinationId = controller.graph.findStartDestination().id,
                            inclusive = false
                        )
                    }
                    is NavigatorAction.Forward -> {
                        val args = action.arguments

                        if (args != null) {
                            val routeLink = NavDeepLinkRequest
                                .Builder
                                .fromUri(NavDestination.createRoute(action.screen.route).toUri())
                                .build()

                            val deepLinkMatch = controller.graph.matchDeepLink(routeLink)

                            if (deepLinkMatch != null) {
                                val destination = deepLinkMatch.destination
                                val id = destination.id

                                controller.navigate(id, args)
                            } else {
                                controller.navigate(action.screen.route)
                            }
                        } else {
                            controller.navigate(action.screen.route)
                        }
                    }
                    is NavigatorAction.ReplaceAll -> {
                        controller.navigate(action.screen.route) { popUpToTop(controller) }

                        action.arguments?.let { arguments ->
                            controller.currentBackStackEntry?.arguments?.putAll(arguments)
                        }
                    }
                }
            }
        }.launchIn(scope = this)
    }
}
