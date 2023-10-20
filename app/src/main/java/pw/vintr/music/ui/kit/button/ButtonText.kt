package pw.vintr.music.ui.kit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonText(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    contentColor: Color = VintrMusicExtendedTheme.colors.textButtonContent,
    disabledContentColor: Color = VintrMusicExtendedTheme.colors.textButtonDisabledContent,
    onClick: () -> Unit,
) {
    ButtonRegular(
        modifier = modifier,
        text = text,
        enabled = enabled,
        isLoading = isLoading,
        color = Color.Transparent,
        contentColor = contentColor,
        disabledColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        onClick = onClick,
    )
}
