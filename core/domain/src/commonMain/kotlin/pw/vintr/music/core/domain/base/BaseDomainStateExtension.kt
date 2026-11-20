package pw.vintr.music.core.domain.base

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T> MutableStateFlow<BaseDomainState<T>>.updateLoaded(
    onDifferentType: () -> Unit = {},
    mutation: (T) -> T
) {
    when (val lockedValue = value) {
        is BaseDomainState.Loaded -> {
            value = lockedValue.copy(data = mutation(lockedValue.data))
        }
        else -> onDifferentType()
    }
}
