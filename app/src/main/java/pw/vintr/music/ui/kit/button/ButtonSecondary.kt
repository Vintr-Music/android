package pw.vintr.music.ui.kit.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.Gilroy12
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

enum class ButtonSecondarySize {
    DEFAULT,
    MEDIUM
}

@Composable
fun ButtonSecondary(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    wrapContentWidth: Boolean = false,
    size: ButtonSecondarySize = ButtonSecondarySize.DEFAULT,
    color: Color = VintrMusicExtendedTheme.colors.secondaryButtonBackground,
    borderColor: Color = VintrMusicExtendedTheme.colors.secondaryButtonStroke,
    contentColor: Color = VintrMusicExtendedTheme.colors.secondaryButtonContent,
    disabledColor: Color = VintrMusicExtendedTheme.colors.secondaryButtonDisabledBackground,
    disabledContentColor: Color = VintrMusicExtendedTheme.colors.secondaryButtonDisabledContent,
    onClick: () -> Unit,
) {
    val height = remember(size) {
        when (size) {
            ButtonSecondarySize.DEFAULT -> 40.dp
            ButtonSecondarySize.MEDIUM -> 30.dp
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .let {
                if (wrapContentWidth) {
                    it.wrapContentWidth()
                } else {
                    it.fillMaxWidth()
                }
            }
            .height(height)
            .defaultMinSize(minHeight = height),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = if (isLoading) color else disabledColor,
            disabledContentColor = disabledContentColor,
        ),
        border = if (enabled && !isLoading) {
            BorderStroke(1.dp, borderColor)
        } else {
            null
        }
    ) {
        if (!isLoading) {
            Text(
                text = text,
                style = when (size) {
                    ButtonSecondarySize.DEFAULT -> RubikMedium16
                    ButtonSecondarySize.MEDIUM -> Gilroy12
                },
                color = if (enabled) { contentColor } else { disabledContentColor },
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor,
            )
        }
    }
}
