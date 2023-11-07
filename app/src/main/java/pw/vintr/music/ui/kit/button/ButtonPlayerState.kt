package pw.vintr.music.ui.kit.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.White0

private const val LABEL = "Player state button"

@Composable
fun ButtonPlayerState(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    size: Dp = 56.dp,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Bee0)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isPlaying,
            label = LABEL
        ) { playerActive ->
            if (playerActive) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = null,
                    tint = White0
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    tint = White0
                )
            }
        }
    }
}
