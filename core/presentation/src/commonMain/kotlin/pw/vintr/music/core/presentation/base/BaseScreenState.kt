package pw.vintr.music.core.presentation.base

import pw.vintr.music.core.domain.base.BaseDomainState

interface BaseScreenState<T> {

    val isLoaded: Boolean
        get() = this is Loaded

    class Loading<T> : BaseScreenState<T>

    class Error<T> : BaseScreenState<T>

    class Empty<T> : BaseScreenState<T>

    data class Loaded<T>(
        val data: T,
        val isRefreshing: Boolean = false,
    ) : BaseScreenState<T>
}

fun <T> mapToScreenState(domainState: BaseDomainState<T>) = when (domainState) {
    is BaseDomainState.Error -> {
        BaseScreenState.Error()
    }
    is BaseDomainState.Loading -> {
        BaseScreenState.Loading()
    }
    is BaseDomainState.Loaded -> {
        val data = domainState.data
        if (data is List<*> && data.isEmpty()) {
            BaseScreenState.Empty()
        } else {
            BaseScreenState.Loaded(
                data = data,
                isRefreshing = domainState.isRefreshing
            )
        }
    }
}
