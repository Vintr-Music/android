package pw.vintr.music.ui.kit.button

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonRegular(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    wrapContentWidth: Boolean = false,
    color: Color = VintrMusicExtendedTheme.colors.regularButtonBackground,
    contentColor: Color = VintrMusicExtendedTheme.colors.regularButtonContent,
    disabledColor: Color = VintrMusicExtendedTheme.colors.regularButtonDisabledBackground,
    disabledContentColor: Color = VintrMusicExtendedTheme.colors.regularButtonDisabledContent,
    onClick: () -> Unit,
) {
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
            .height(40.dp)
            .defaultMinSize(minHeight = 40.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = if (isLoading) color else disabledColor,
            disabledContentColor = disabledContentColor,
        )
    ) {
        if (!isLoading) {
            Text(
                text = text,
                style = RubikMedium16,
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
