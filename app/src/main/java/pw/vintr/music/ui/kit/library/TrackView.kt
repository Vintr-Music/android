package pw.vintr.music.ui.kit.library

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun TrackView(
    modifier: Modifier = Modifier,
    trackModel: TrackModel,
    isPlaying: Boolean = false,
    showArtwork: Boolean = false,
    showTrailingAction: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 4.dp,
        horizontal = 20.dp
    ),
    onMoreClick: () -> Unit = {},
    trailingAction: @Composable () -> Unit = {
        ButtonSimpleIcon(
            iconRes = R.drawable.ic_more,
            onClick = onMoreClick,
            tint = VintrMusicExtendedTheme.colors.textRegular,
        )
    },
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showArtwork) {
            BoxWithConstraints(
                modifier = Modifier
                    .size(48.dp)
                    .artworkContainer(RoundedCornerShape(4.dp)),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trackModel.artworkUrl)
                        .size(
                            width = constraints.maxWidth,
                            height = constraints.maxHeight
                        )
                        .crossfade(enable = true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
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
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = trackModel.format.durationFormat,
            style = RubikRegular14,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        if (showTrailingAction) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingAction()
        }
    }
}
