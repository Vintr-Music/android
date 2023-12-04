package pw.vintr.music.ui.kit.library

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    isPlaying: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(vertical = 4.dp, horizontal = 20.dp),
    onMoreClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isPlaying) {
            VintrMusicExtendedTheme.colors.trackHighlight
        } else {
            Color.Transparent
        },
        label = "Track background color"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor.value)
            .clickable { onClick() }
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = trackModel.metadata.title,
                style = RubikMedium16,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            Spacer(modifier = Modifier.height(4.dp))
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
