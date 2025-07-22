package pw.vintr.music.tools.extension

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.shouldStartPaginate(
    itemsCount: Int? = null,
    offset: Int = 2,
    isLastPage: Boolean = false,
): Boolean {

    if (isLastPage) {
        return false
    }

    val lastVisibleIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1)
    val lastIndex = (itemsCount ?: layoutInfo.totalItemsCount) - 1 - offset

    return lastVisibleIndex >= lastIndex
}

suspend fun LazyListState.scrollToTop() {
    scrollToItem(0)
}
