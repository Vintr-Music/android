package pw.vintr.music.tools.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
inline fun <T> ColumnScope.ColumnItems(
    items: List<T>,
    spacing: Dp = 0.dp,
    noinline divider: @Composable (() -> Unit)? = null,
    crossinline itemContent: @Composable ColumnScope.(item: T) -> Unit
) {
    if (spacing.value > 0 || divider != null) {
        items.forEachIndexed { index, item ->
            // Item
            itemContent(item)
            // Spacing
            if (index != items.lastIndex) {
                if (divider != null) {
                    divider()
                } else {
                    Spacer(modifier = Modifier.height(spacing))
                }
            }
        }
    } else {
        items.forEach {item -> itemContent(item) }
    }
}
