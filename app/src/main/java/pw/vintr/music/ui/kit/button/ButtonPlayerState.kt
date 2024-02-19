package pw.vintr.music.ui.kit.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme
import pw.vintr.music.ui.theme.White0

private const val LABEL = "Player state button"

private enum class ButtonState {
    IDLE,
    LOADING,
    PLAYING,
}

@Composable
fun ButtonPlayerState(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    isLoading: Boolean = false,
    size: Dp = 56.dp,
    onClick: () -> Unit = {},
) {
    val state = remember(isPlaying, isLoading) {
        when {
            isPlaying -> ButtonState.PLAYING
            isLoading -> ButtonState.LOADING
            else -> ButtonState.IDLE
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Bee0)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = state,
            label = LABEL
        ) { buttonState ->
            when (buttonState) {
                ButtonState.IDLE -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = null,
                        tint = White0
                    )
                }
                ButtonState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        color = White0
                    )
                }
                ButtonState.PLAYING -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = null,
                        tint = White0
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonPlayerStateMini(
    isPlaying: Boolean = false,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
) {
    val state = remember(isPlaying, isLoading) {
        when {
            isPlaying -> ButtonState.PLAYING
            isLoading -> ButtonState.LOADING
            else -> ButtonState.IDLE
        }
    }

    Box(
        modifier = Modifier
            .size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = state,
            label = LABEL
        ) { buttonState ->
            when (buttonState) {
                ButtonState.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        color = VintrMusicExtendedTheme.colors.textRegular,
                    )
                }
                ButtonState.IDLE -> {
                    ButtonSimpleIcon(
                        iconRes = R.drawable.ic_play,
                        tint = VintrMusicExtendedTheme.colors.textRegular,
                        onClick = onClick
                    )
                }
                ButtonState.PLAYING -> {
                    ButtonSimpleIcon(
                        iconRes = R.drawable.ic_pause,
                        tint = VintrMusicExtendedTheme.colors.textRegular,
                        onClick = onClick
                    )
                }
            }
        }
    }
}
