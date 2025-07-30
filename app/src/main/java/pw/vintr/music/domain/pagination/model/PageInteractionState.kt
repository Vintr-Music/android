package pw.vintr.music.domain.pagination.model

import kotlinx.coroutines.flow.MutableStateFlow

sealed interface PageInteractionState {

    data object Loading : PageInteractionState

    data object Error : PageInteractionState

    data class Succeed(
        val hasFailedPage: Boolean,
        val loadingNextPage: Boolean,
    ) : PageInteractionState
}

suspend fun MutableStateFlow<PageInteractionState>.withFirstPageLoading(
    action: suspend () -> Unit
) {
    try {
        value = PageInteractionState.Loading
        action()
        value = PageInteractionState.Succeed(
            hasFailedPage = false,
            loadingNextPage = false
        )
    } catch (exception: Throwable) {
        value = PageInteractionState.Error

        throw exception
    }
}

suspend fun MutableStateFlow<PageInteractionState>.withNextPageLoading(
    action: suspend () -> Unit
) {
    try {
        value = PageInteractionState.Succeed(
            hasFailedPage = false,
            loadingNextPage = true
        )
        action()
        value = PageInteractionState.Succeed(
            hasFailedPage = false,
            loadingNextPage = false
        )
    } catch (exception: Throwable) {
        value = PageInteractionState.Succeed(
            hasFailedPage = true,
            loadingNextPage = false
        )

        throw exception
    }
}
