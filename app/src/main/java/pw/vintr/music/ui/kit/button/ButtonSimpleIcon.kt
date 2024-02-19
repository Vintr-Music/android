package pw.vintr.music.ui.kit.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonSimpleIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    @DrawableRes
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
            painter = painterResource(id = iconRes),
            contentDescription = "Simple icon button",
            tint = tint
        )
    }
}
