package pw.vintr.music.tools.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

inline fun <reified R> StateFlow<*>.withTyped(block: (R) -> Unit) {
    val lockedValue = value

    if (lockedValue is R) {
        block(lockedValue)
    }
}

inline fun <T: Any, reified R : T> MutableStateFlow<T>.updateTyped(mutation: (R) -> R) {
    withTyped<R> { value = it.let(mutation) }
}
