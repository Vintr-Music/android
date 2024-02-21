package pw.vintr.music.tools.composable

import android.os.Build
import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.app.main.LocalActivity

@Composable
fun rememberDisplayRoundness(defaultValue: Dp = 0.dp): Dp {
    val insets = LocalActivity.current.window.decorView.rootWindowInsets
    val density = LocalDensity.current

    return remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            insets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)?.let {
                with(density) { it.radius.toDp() }
            } ?: 0.dp
        } else {
            defaultValue
        }
    }
}
