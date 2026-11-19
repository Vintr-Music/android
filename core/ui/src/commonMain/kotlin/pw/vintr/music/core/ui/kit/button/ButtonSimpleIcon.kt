package pw.vintr.music.core.ui.kit.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pw.vintr.music.core.ui.kit.resource.drawablePainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonSimpleIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    iconRes: Int,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    tint: Color = VintrMusicExtendedTheme.colors.textRegular
) {
    IconButton(
        modifier = modifier.size(size),
        onClick = onClick
    ) {
        Icon(
            modifier = iconModifier,
            painter = drawablePainter(iconRes),
            contentDescription = "Simple icon button",
            tint = tint
        )
    }
}
