package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.ui.kit.button.ButtonPlayerStateMini
import pw.vintr.music.ui.kit.separator.LineSeparator
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun BottomNowPlaying(
    modifier: Modifier = Modifier,
    state: PlayerStateHolderModel,
    onClick: () -> Unit = {},
    onControlClick: () -> Unit,
    onSeekToTrack: (Int) -> Unit,
) {
    val playerStatus = state.status

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
    ) {
        LineSeparator()
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.currentTrack != null) {
                PlayerStatePager(
                    modifier = Modifier.weight(1f),
                    state = state,
                    onSeekToTrack = onSeekToTrack,
                ) { track, _ ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = track.metadata.title,
                            style = RubikMedium16,
                            color = VintrMusicExtendedTheme.colors.textRegular,
                            maxLines = 1,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = track.metadata.artist,
                            style = RubikRegular14,
                            color = VintrMusicExtendedTheme.colors.textSecondary,
                            maxLines = 1,
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.width(8.dp))
            ButtonPlayerStateMini(
                isPlaying = playerStatus == PlayerStatusModel.PLAYING,
                isLoading = playerStatus == PlayerStatusModel.LOADING,
                onClick = { onControlClick() }
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
