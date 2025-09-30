package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.ui.kit.button.ButtonPlayerStateIcon
import pw.vintr.music.ui.theme.Gilroy11
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
@Preview
fun FlowPlayer(
    modifier: Modifier = Modifier,
    playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE,
    currentSessionIsFlow: Boolean = false,
    onChangePlayerState: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonPlayerStateIcon(
                size = 42.dp,
                isPlaying = playerStatus == PlayerStatusModel.PLAYING,
                isLoading = playerStatus == PlayerStatusModel.LOADING,
                onClick = { onChangePlayerState() },
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = stringResource(R.string.music_flow_title),
                color = VintrMusicExtendedTheme.colors.textRegular,
                style = Gilroy24,
            )
        }

        if (currentSessionIsFlow) {
            Spacer(Modifier.height(20.dp))
            // Shuffle pill button to re-shake existing session
            Row(
                modifier = Modifier
                    .height(32.dp)
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
                    .clickable { onShuffleClick() }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(R.drawable.ic_shuffle),
                    tint = VintrMusicExtendedTheme.colors.textSecondary,
                    contentDescription = "Shuffle icon"
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.music_flow_shuffle),
                    color = VintrMusicExtendedTheme.colors.textSecondary,
                    style = Gilroy11,
                )
            }
        }
    }
}
