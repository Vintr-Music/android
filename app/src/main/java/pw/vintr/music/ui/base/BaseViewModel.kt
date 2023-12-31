package pw.vintr.music.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import pw.vintr.music.ui.navigation.Navigator
import org.koin.core.component.inject
import pw.vintr.music.ui.navigation.NavigatorType

abstract class BaseViewModel : ViewModel(), CoroutineScope, KoinComponent {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + job

    protected val navigator: Navigator by inject()

    open fun navigateBack(type: NavigatorType? = null) { navigator.back(type) }

    protected fun createExceptionHandler(
        onException: (Throwable) -> Unit = { }
    ) = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onException.invoke(throwable)
    }

    protected fun <T> MutableStateFlow<BaseScreenState<T>>.loadWithStateHandling(
        block: suspend () -> T
    ) = launch(createExceptionHandler { value = BaseScreenState.Error() }) {
        value = BaseScreenState.Loading()
        value = BaseScreenState.Loaded(block())
    }

    protected fun <T> MutableStateFlow<BaseScreenState<T>>.refreshWithStateHandling(
        block: suspend () -> T
    ) = launch(createExceptionHandler { value = BaseScreenState.Error() }) {
        val lockedValue = value

        if (lockedValue is BaseScreenState.Loaded) {
            value = BaseScreenState.Loaded(lockedValue.data, isRefreshing = true)
            value = BaseScreenState.Loaded(block())
        }
    }

    protected fun <T> Flow<T>.stateInThis(initialValue: T) = stateIn(
        scope = this@BaseViewModel,
        started = SharingStarted.Lazily,
        initialValue = initialValue,
    )

    override fun onCleared() {
        if (isActive) cancel()
        super.onCleared()
    }
}
