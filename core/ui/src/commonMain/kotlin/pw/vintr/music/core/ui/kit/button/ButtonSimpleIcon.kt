package pw.vintr.music.core.ui.kit.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pw.vintr.music.core.ui.kit.resource.drawablePainter
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
    ButtonSimpleIcon(
        modifier = modifier,
        iconModifier = iconModifier,
        painter = drawablePainter(iconRes),
        onClick = onClick,
        size = size,
        tint = tint,
    )
}

@Composable
fun ButtonSimpleIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: DrawableResource,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    tint: Color = VintrMusicExtendedTheme.colors.textRegular
) {
    ButtonSimpleIcon(
        modifier = modifier,
        iconModifier = iconModifier,
        painter = painterResource(icon),
        onClick = onClick,
        size = size,
        tint = tint,
    )
}

@Composable
fun ButtonSimpleIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    painter: Painter,
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
            painter = painter,
            contentDescription = "Simple icon button",
            tint = tint
        )
    }
}
