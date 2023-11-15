package pw.vintr.music.tools.composable

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun rememberSystemNavigationBarHeight(): Dp {
    val context = LocalContext.current
    val density = LocalDensity.current

    return remember {
        val dimension = context.resources.getDimensionPixelSize(
            context.resources.getIdentifier(
                "navigation_bar_height",
                "dimen",
                "android",
            )
        )
        with(density) { dimension.toDp() }
    }
}
