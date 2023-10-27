package pw.vintr.music.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
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
    val textButtonDisabledContent: Color = Color.Unspecified,
    // Text Field
    val textFieldBackground: Color = Color.Unspecified,
    val textFieldContent: Color = Color.Unspecified,
    val textFieldHint: Color = Color.Unspecified,
    val textFieldLabel: Color = Color.Unspecified,
    // Text
    val textRegular: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    // Radio button
    val radioBackground: Color = Color.Unspecified,
    val radioStroke: Color = Color.Unspecified,
    val radioUnselected: Color = Color.Unspecified,
    val radioSelected: Color = Color.Unspecified,
    val radioDisabled: Color = Color.Unspecified,
    // Ripple
    val ripple: Color = Color.Unspecified,
    // Nav bar
    val navBarSelected: Color = Color.Unspecified,
    val navBarUnselected: Color = Color.Unspecified,
)

val LocalVintrColors = staticCompositionLocalOf { VintrMusicColors() }

val darkVintrColors = VintrMusicColors(
    // Regular button
    regularButtonBackground = Bee1,
    regularButtonContent = White0,
    regularButtonDisabledBackground = Gray1,
    regularButtonDisabledContent = Gray3,
    // Text button
    textButtonContent = White0,
    textButtonDisabledContent = Gray3,
    // Text Field
    textFieldBackground = Gray1,
    textFieldContent = White0,
    textFieldHint = Gray3,
    textFieldLabel = Bee2,
    // Text
    textRegular = White0,
    textSecondary = Gray4,
    // Radio button
    radioBackground = Gray1,
    radioStroke = Gray3,
    radioSelected = Gray5,
    // Ripple
    ripple = Gray5,
    // Nav bar
    navBarSelected = Bee1,
    navBarUnselected = White0,
)

val lightVintrColors = VintrMusicColors(
    // Regular button
    regularButtonBackground = Bee1,
    regularButtonContent = White0,
    regularButtonDisabledBackground = Gray5,
    regularButtonDisabledContent = Gray3,
    // Text button
    textButtonContent = Black0,
    textButtonDisabledContent = Gray3,
    // Text Field
    textFieldBackground = Gray6,
    textFieldContent = White0,
    textFieldHint = Gray3,
    textFieldLabel = Bee2,
    // Text
    textRegular = Black0,
    textSecondary = Gray3,
    // Radio button
    radioBackground = Gray6,
    radioStroke = Gray4,
    radioSelected = Gray2,
    // Ripple
    ripple = Gray2,
    // Nav bar
    navBarSelected = Bee0,
    navBarUnselected = Black0,
)

private val darkColorScheme = darkColorScheme(
    primary = Black0,
    secondary = Bee1,
    tertiary = Gray1,
    background = Black0,
)

private val lightColorScheme = lightColorScheme(
    primary = White0,
    secondary = Bee1,
    tertiary = Gray6,
    background = White0,
)

object VintrMusicRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = LocalVintrColors.current.ripple

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        LocalVintrColors.current.ripple,
        lightTheme = false,
    )
}

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
        ) {
            CompositionLocalProvider(
                LocalRippleTheme provides VintrMusicRippleTheme,
                content = content
            )
        }
    }
}

object VintrMusicExtendedTheme {
    val colors: VintrMusicColors
        @Composable
        get() = LocalVintrColors.current
}
