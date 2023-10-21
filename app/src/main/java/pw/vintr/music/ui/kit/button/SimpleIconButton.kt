package pw.vintr.music.ui.kit.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes
    iconRes: Int,
    onClick: () -> Unit,
    size: Dp = 24.dp,
    tint: Color = LocalContentColor.current
) {
    IconButton(
        modifier = modifier.size(size),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "Simple icon button",
            tint = tint
        )
    }
}
