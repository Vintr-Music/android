package pw.vintr.music.tools.extension

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

inline fun <T> LazyListScope.itemsSeparated(
    items: List<T>,
    includeLastDivider: Boolean = false,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemSeparator: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null,
    contentType = { index -> contentType(index, items[index]) }
) {
    itemContent(it, items[it])
    if (includeLastDivider || it < items.size) {
        itemSeparator(it, items[it])
    }
}
