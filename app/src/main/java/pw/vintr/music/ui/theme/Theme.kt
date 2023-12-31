package pw.vintr.music.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
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
    // Library
    val trackHighlight: Color = Color.Unspecified,
    val coverBackground: Color = Color.Unspecified,
    // Common
    val lineSeparator: Color = Color.Unspecified,
    // Player Slider
    val playerSliderActiveBackground: Color = Color.Unspecified,
    val playerSliderInactiveBackground: Color = Color.Unspecified,
    val playerSliderStroke: Color = Color.Unspecified,
    // Card
    val cardBackground: Color = Color.Unspecified,
    val cardStroke: Color = Color.Unspecified,
    // Equalizer
    val equalizerSliderBackground: Color = Color.Unspecified,
    val equalizerInactiveSliderColor: Color = Color.Unspecified,
    // Switch
    val switchActiveThumbColor: Color = Color.Unspecified,
    val switchActiveBackgroundColor: Color = Color.Unspecified,
    val switchInactiveThumbColor: Color = Color.Unspecified,
    val switchInactiveBackgroundColor: Color = Color.Unspecified,
    // BottomSheet
    val dialogBackground: Color = Color.Unspecified,
    val dialogStroke: Color = Color.Unspecified,
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
    // Library
    trackHighlight = Gray3.copy(alpha = 0.3f),
    coverBackground = Gray1,
    // Common
    lineSeparator = Gray1,
    // Player Slider
    playerSliderActiveBackground = Black0,
    playerSliderInactiveBackground = Gray0,
    playerSliderStroke = Gray1,
    // Card
    cardBackground = Card0,
    cardStroke = Gray2,
    // Equalizer
    equalizerSliderBackground = Gray1,
    equalizerInactiveSliderColor = Gray2,
    // Switch
    switchActiveThumbColor = Gray1,
    switchActiveBackgroundColor = Gray5,
    switchInactiveBackgroundColor = Gray1,
    switchInactiveThumbColor = Gray5,
    // BottomSheet
    dialogBackground = Card2,
    dialogStroke = Gray1,
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
    // Library
    trackHighlight = Gray3.copy(alpha = 0.1f),
    coverBackground = Gray4,
    // Common
    lineSeparator = Gray5,
    // Player Slider
    playerSliderActiveBackground = White0,
    playerSliderInactiveBackground = Gray5,
    playerSliderStroke = Gray4,
    // Card
    cardBackground = Card1,
    cardStroke = Gray5,
    // Equalizer
    equalizerSliderBackground = Gray5,
    equalizerInactiveSliderColor = Gray4,
    // Switch
    switchActiveThumbColor = White0,
    switchActiveBackgroundColor = Bee0,
    switchInactiveBackgroundColor = White0,
    switchInactiveThumbColor = Bee0,
    // BottomSheet
    dialogBackground = Gray5,
    dialogStroke = Gray4,
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

@Composable
fun switchColors() = SwitchDefaults.colors(
    checkedThumbColor = VintrMusicExtendedTheme.colors.switchActiveThumbColor,
    uncheckedThumbColor = VintrMusicExtendedTheme.colors.switchInactiveThumbColor,
    checkedTrackColor = VintrMusicExtendedTheme.colors.switchActiveBackgroundColor,
    uncheckedTrackColor = VintrMusicExtendedTheme.colors.switchInactiveBackgroundColor,
    checkedBorderColor = VintrMusicExtendedTheme.colors.switchActiveBackgroundColor,
    uncheckedBorderColor = VintrMusicExtendedTheme.colors.switchActiveBackgroundColor,
    disabledCheckedThumbColor = VintrMusicExtendedTheme.colors.switchActiveThumbColor
        .copy(alpha = 0.5f),
    disabledCheckedTrackColor = VintrMusicExtendedTheme.colors.switchActiveBackgroundColor
        .copy(alpha = 0.5f),
    disabledUncheckedThumbColor = VintrMusicExtendedTheme.colors.switchInactiveThumbColor
        .copy(alpha = 0.5f),
    disabledUncheckedTrackColor = VintrMusicExtendedTheme.colors.switchInactiveBackgroundColor
        .copy(alpha = 0.5f),
)
