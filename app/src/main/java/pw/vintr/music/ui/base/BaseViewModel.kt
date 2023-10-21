package pw.vintr.music.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import org.koin.core.component.KoinComponent
import pw.vintr.music.ui.navigation.Navigator
import org.koin.core.component.inject

abstract class BaseViewModel : ViewModel(), CoroutineScope, KoinComponent {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + job

    protected val navigator: Navigator by inject()

    fun navigateBack() { navigator.back() }

    protected fun createExceptionHandler(
        onException: (Throwable) -> Unit = { }
    ) = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onException.invoke(throwable)
    }

    override fun onCleared() {
        if (isActive) cancel()
        super.onCleared()
    }
}
