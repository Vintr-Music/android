package pw.vintr.music.ui.kit.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonBorderedIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    @DrawableRes
    iconRes: Int,
    onClick: () -> Unit,
    size: Dp = 32.dp,
    tint: Color = VintrMusicExtendedTheme.colors.textRegular
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(10.dp))
            .background(
                VintrMusicExtendedTheme.colors.borderedIconButtonBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                BorderStroke(
                    1.dp,
                    VintrMusicExtendedTheme.colors.borderedIconButtonStroke
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = iconModifier,
            painter = painterResource(id = iconRes),
            contentDescription = "Simple icon button",
            tint = tint
        )
    }
}
