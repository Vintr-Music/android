package pw.vintr.music.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + job

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
