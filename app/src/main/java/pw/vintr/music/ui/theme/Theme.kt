package pw.vintr.music.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class VintrMusicColors(
    // Regular button
    val regularButtonBackground: Color = Color.Unspecified,
    val regularButtonContent: Color = Color.Unspecified,
    val regularButtonDisabledBackground: Color = Color.Unspecified,
    val regularButtonDisabledContent: Color = Color.Unspecified,
    // Text button
    val textButtonContent: Color = Color.Unspecified,
    // Text Field
    val textFieldBackground: Color = Color.Unspecified,
    val textFieldContent: Color = Color.Unspecified,
    val textFieldLabel: Color = Color.Unspecified
)

val LocalVintrColors = staticCompositionLocalOf { VintrMusicColors() }

val darkVintrColors = VintrMusicColors(
    // Regular button
    regularButtonBackground = Bee0,
    regularButtonContent = White0,
    regularButtonDisabledBackground = Gray1,
    regularButtonDisabledContent = Gray3,
    // Text button
    textButtonContent = White0,
    // Text Field
    textFieldBackground = Gray1,
    textFieldContent = White0,
    textFieldLabel = Bee1,
)

val lightVintrColors = VintrMusicColors(
    // Regular button
    regularButtonBackground = Bee0,
    regularButtonContent = White0,
    regularButtonDisabledBackground = Gray5,
    regularButtonDisabledContent = Gray3,
    // Text button
    textButtonContent = White0,
    // Text Field
    textFieldBackground = Gray1,
    textFieldContent = White0,
    textFieldLabel = Bee1,
)

private val darkColorScheme = darkColorScheme(
    primary = Black0,
    secondary = Bee0,
    tertiary = Gray1,
    background = Black0
)

private val lightColorScheme = lightColorScheme(
    primary = White0,
    secondary = Bee0,
    tertiary = Gray45,
    background = White0,
)

@Composable
fun VintrMusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val colorScheme = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }
    val vintrColors = if (darkTheme) {
        darkVintrColors
    } else {
        lightVintrColors
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val windowsInsetsController = WindowCompat.getInsetsController(window, view)

            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            windowsInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowsInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalVintrColors provides vintrColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object VintrMusicExtendedTheme {
    val colors: VintrMusicColors
        @Composable
        get() = LocalVintrColors.current
}
