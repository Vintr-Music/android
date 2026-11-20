package pw.vintr.music.core.presentation.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.vintr.music.core.presentation.base.BaseScreenState

inline fun <reified R> StateFlow<*>.withTyped(block: (R) -> Unit) {
    val lockedValue = value

    if (lockedValue is R) {
        block(lockedValue)
    }
}

inline fun <T> StateFlow<BaseScreenState<T>>.withLoaded(block: (T) -> Unit) {
    withTyped<BaseScreenState.Loaded<T>> { block(it.data) }
}

inline fun <reified R> MutableStateFlow<in R>.updateTyped(
    onDifferentType: () -> Unit = {},
    mutation: (R) -> R
) {
    val lockedValue = value

    if (lockedValue is R) {
        value = mutation(lockedValue)
    } else {
        onDifferentType()
    }
}

inline fun <T> MutableStateFlow<BaseScreenState<T>>.updateLoaded(
    onDifferentType: () -> Unit = {},
    mutation: (T) -> T,
) {
    updateTyped<BaseScreenState.Loaded<T>>(onDifferentType) { loaded ->
        loaded.copy(data = mutation(loaded.data))
    }
}

inline fun <T> MutableStateFlow<BaseScreenState<T>>.updateWithLoaded(
    onDifferentType: () -> Unit = {},
    mutation: (T) -> BaseScreenState<T>,
) {
    val lockedValue = value

    if (lockedValue is BaseScreenState.Loaded<T>) {
        value = mutation(lockedValue.data)
    } else {
        onDifferentType()
    }
}
