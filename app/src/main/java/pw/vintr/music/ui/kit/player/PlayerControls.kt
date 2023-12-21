package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
@Preview(widthDp = 400)
fun PlayerControls(
    playerState: PlayerStateHolderModel = PlayerStateHolderModel(),
    onBackward: () -> Unit = {},
    onChangePlayerState: () -> Unit = {},
    onForward: () -> Unit = {},
    onSetNextRepeatMode: (PlayerRepeatMode) -> Unit = {},
    onSetNextShuffleMode: (PlayerShuffleMode) -> Unit = {},
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(20.dp)
            .navigationBarsPadding()
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
            .border(
                1.dp,
                VintrMusicExtendedTheme.colors.playerSliderStroke,
                RoundedCornerShape(20.dp)
            )
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        val width = with (LocalDensity.current) {
            constraints.maxWidth
                .toDp()
                .coerceAtMost(340.dp)
        }

        Row(
            modifier = Modifier
                .width(width),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SimpleIconButton(
                iconRes = when (playerState.repeatMode) {
                    PlayerRepeatMode.OFF,
                    PlayerRepeatMode.ON_SESSION -> R.drawable.ic_repeat
                    PlayerRepeatMode.ON_SINGLE -> R.drawable.ic_repeat_single
                },
                size = 40.dp,
                iconModifier = Modifier.size(28.dp),
                tint = when (playerState.repeatMode) {
                    PlayerRepeatMode.OFF -> VintrMusicExtendedTheme.colors.textRegular
                    PlayerRepeatMode.ON_SESSION,
                    PlayerRepeatMode.ON_SINGLE -> Bee0
                },
                onClick = { onSetNextRepeatMode(playerState.repeatMode) },
            )
            SimpleIconButton(
                iconRes = R.drawable.ic_skip_backward,
                size = 40.dp,
                iconModifier = Modifier.size(28.dp),
                onClick = { onBackward() }
            )
            ButtonPlayerState(
                isPlaying = playerState.status == PlayerStatusModel.PLAYING,
                isLoading = playerState.status == PlayerStatusModel.LOADING,
                onClick = { onChangePlayerState() }
            )
            SimpleIconButton(
                iconRes = R.drawable.ic_skip_forward,
                size = 40.dp,
                iconModifier = Modifier.size(28.dp),
                onClick = { onForward() }
            )
            SimpleIconButton(
                iconRes = R.drawable.ic_shuffle,
                size = 40.dp,
                iconModifier = Modifier.size(28.dp),
                tint = when (playerState.shuffleMode) {
                    PlayerShuffleMode.OFF -> VintrMusicExtendedTheme.colors.textRegular
                    PlayerShuffleMode.ON -> Bee0
                },
                onClick = { onSetNextShuffleMode(playerState.shuffleMode) },
            )
        }
    }
}
