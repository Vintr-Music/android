package pw.vintr.music.tools.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.vintr.music.domain.base.BaseDomainState
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

@JvmName("updateLoadedScreen")
inline fun <T> MutableStateFlow<BaseScreenState<T>>.updateLoaded(
    onDifferentType: () -> Unit = {},
    mutation: (T) -> T,
) {
    updateTyped<BaseScreenState.Loaded<T>>(onDifferentType) { loaded ->
        loaded.copy(data = mutation(loaded.data))
    }
}

@JvmName("updateWithLoadedScreen")
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

@JvmName("updateLoadedDomain")
inline fun <T> MutableStateFlow<BaseDomainState<T>>.updateLoaded(
    onDifferentType: () -> Unit = {},
    mutation: (T) -> T
) {
    updateTyped<BaseDomainState.Loaded<T>>(onDifferentType) { loaded ->
        loaded.copy(data = mutation(loaded.data))
    }
}

inline fun <reified R> MutableStateFlow<List<R>>.addItem(item: R) {
    value = value
        .toMutableList()
        .apply { add(item) }
}

inline fun <reified R> MutableStateFlow<List<R>>.removeItem(item: R) {
    value = value
        .toMutableList()
        .apply { remove(item) }
}
