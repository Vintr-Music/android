package pw.vintr.music.core.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ApplySystemBars(darkTheme: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return

    SideEffect {
        val window = (view.context as Activity).window
        val windowsInsetsController = WindowCompat.getInsetsController(window, view)

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        windowsInsetsController.isAppearanceLightStatusBars = !darkTheme
        windowsInsetsController.isAppearanceLightNavigationBars = !darkTheme
    }
}
