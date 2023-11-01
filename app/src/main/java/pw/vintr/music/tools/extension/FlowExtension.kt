package pw.vintr.music.tools.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.vintr.music.ui.base.BaseScreenState

inline fun <reified R> StateFlow<*>.withTyped(block: (R) -> Unit) {
    val lockedValue = value

    if (lockedValue is R) {
        block(lockedValue)
    }
}

inline fun <T> StateFlow<BaseScreenState<T>>.withLoaded(block: (T) -> Unit) {
    withTyped<BaseScreenState.Loaded<T>> { block(it.data) }
}

inline fun <reified R> MutableStateFlow<in R>.updateTyped(mutation: (R) -> R) {
    val lockedValue = value

    if (lockedValue is R) {
        value = mutation(lockedValue)
    }
}

inline fun <T> MutableStateFlow<BaseScreenState<T>>.updateLoaded(mutation: (T) -> T) {
    updateTyped<BaseScreenState.Loaded<T>> { loaded ->
        loaded.copy(data = mutation(loaded.data))
    }
}
