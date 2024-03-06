package pw.vintr.music.domain.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import java.io.Closeable

abstract class BaseInteractor : CoroutineScope, Closeable {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + job

    protected suspend fun <T> MutableStateFlow<BaseDomainState<T>>.loadWithStateHandling(
        block: suspend () -> T
    ) {
        runCatching {
            value = BaseDomainState.Loading()
            value = BaseDomainState.Loaded(block())
        }.onFailure {
            it.printStackTrace()
            value = BaseDomainState.Error()
        }
    }

    protected suspend fun <T> MutableStateFlow<BaseDomainState<T>>.refreshWithStateHandling(
        block: suspend () -> T
    ) {
        val lockedValue = value

        runCatching {
            if (lockedValue is BaseDomainState.Loaded) {
                value = BaseDomainState.Loaded(lockedValue.data, isRefreshing = true)
                value = BaseDomainState.Loaded(block())
            }
        }.onFailure {
            it.printStackTrace()
            value = BaseDomainState.Error()
        }
    }

    override fun close() {
        if (isActive) cancel()
    }
}
