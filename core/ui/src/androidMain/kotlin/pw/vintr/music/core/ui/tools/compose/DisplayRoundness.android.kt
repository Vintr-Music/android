package pw.vintr.music.core.ui.tools.compose

import android.os.Build
import android.view.RoundedCorner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp

@Composable
actual fun rememberDisplayRoundness(defaultValue: Dp): Dp {
    val view = LocalView.current
    val density = LocalDensity.current

    return remember(view, density, defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            view.rootWindowInsets?.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)?.let {
                with(density) { it.radius.toDp() }
            } ?: defaultValue
        } else {
            defaultValue
        }
    }
}
