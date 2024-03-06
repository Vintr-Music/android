package pw.vintr.music.domain.base

sealed interface BaseDomainState<T> {

    class Loading<T> : BaseDomainState<T>

    class Error<T> : BaseDomainState<T>

    data class Loaded<T>(
        val data: T,
        val isRefreshing: Boolean = false,
    ) : BaseDomainState<T>
}
