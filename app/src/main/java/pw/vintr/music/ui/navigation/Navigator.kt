package pw.vintr.music.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {

    private val _actionFlow = MutableSharedFlow<NavigatorAction>(extraBufferCapacity = 1)

    val actionFlow = _actionFlow.asSharedFlow()

    fun back() {
        _actionFlow.tryEmit(NavigatorAction.Back)
    }

    fun forward(screen: Screen) {
        _actionFlow.tryEmit(NavigatorAction.Forward(screen))
    }

    fun replaceAll(screen: Screen) {
        _actionFlow.tryEmit(NavigatorAction.ReplaceAll(screen))
    }
}

sealed class NavigatorAction {
    object Back : NavigatorAction()

    data class Forward(val screen: Screen) : NavigatorAction()

    data class ReplaceAll(val screen: Screen) : NavigatorAction()
}
