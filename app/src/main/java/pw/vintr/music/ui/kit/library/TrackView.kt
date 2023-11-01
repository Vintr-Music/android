package pw.vintr.music.ui.kit.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun TrackView(
    modifier: Modifier = Modifier,
    trackModel: TrackModel,
    onMoreClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = trackModel.metadata.title,
                style = RubikMedium16,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            Text(
                text = trackModel.metadata.artist,
                style = RubikRegular14,
                color = VintrMusicExtendedTheme.colors.textSecondary
            )
        }
        Text(
            text = trackModel.format.durationFormat,
            style = RubikRegular14,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        SimpleIconButton(
            iconRes = R.drawable.ic_more,
            onClick = onMoreClick,
            tint = VintrMusicExtendedTheme.colors.textRegular,
        )
    }
}
