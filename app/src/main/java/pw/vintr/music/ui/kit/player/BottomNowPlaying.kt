package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.PlayerStatusModel
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.kit.separator.LineSeparator
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun BottomNowPlaying(
    modifier: Modifier = Modifier,
    track: TrackModel?,
    playerStatus: PlayerStatusModel,
    onClick: () -> Unit = {},
    onControlClick: () -> Unit,
) {
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
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = track?.metadata?.title.orEmpty(),
                    style = RubikMedium16,
                    color = VintrMusicExtendedTheme.colors.textRegular,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = track?.metadata?.artist.orEmpty(),
                    style = RubikRegular14,
                    color = VintrMusicExtendedTheme.colors.textSecondary,
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SimpleIconButton(
                iconRes = when (playerStatus) {
                    PlayerStatusModel.IDLE,
                    PlayerStatusModel.PAUSED -> R.drawable.ic_play
                    PlayerStatusModel.PLAYING -> R.drawable.ic_pause
                },
                tint = VintrMusicExtendedTheme.colors.textRegular,
                onClick = { onControlClick() }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LineSeparator()
    }
}
