package pw.vintr.music.ui.base

import pw.vintr.music.domain.base.BaseDomainState

interface BaseScreenState<T> {

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
        if (domainState.data is List<*>) {
            if (domainState.data.isNotEmpty()) {
                BaseScreenState.Loaded(
                    data = domainState.data,
                    isRefreshing = domainState.isRefreshing
                )
            } else {
                BaseScreenState.Empty()
            }
        } else {
            BaseScreenState.Loaded(
                data = domainState.data,
                isRefreshing = domainState.isRefreshing
            )
        }
    }
}
