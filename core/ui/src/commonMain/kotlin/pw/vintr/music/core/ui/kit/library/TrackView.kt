package pw.vintr.music.core.ui.kit.library

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.size.Size
import pw.vintr.music.core.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.core.ui.kit.image.ArtworkAsyncImage
import pw.vintr.music.core.ui.kit.modifier.artworkContainer
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.ic_more
import pw.vintr.music.core.ui.theme.RubikMedium16
import pw.vintr.music.core.ui.theme.RubikRegular14
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

const val FIXED_TRACK_VIEW_HEIGHT = 56

@Composable
fun TrackView(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
    durationText: String,
    artworkUrl: String? = null,
    isPlaying: Boolean = false,
    showArtwork: Boolean = false,
    showTrailingAction: Boolean = true,
    fixedHeight: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 4.dp,
        horizontal = 20.dp
    ),
    onMoreClick: () -> Unit = {},
    trailingAction: @Composable () -> Unit = {
        ButtonSimpleIcon(
            icon = Res.drawable.ic_more,
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
                ArtworkAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    url = artworkUrl,
                    size = Size(
                        width = constraints.maxWidth,
                        height = constraints.maxHeight
                    ),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = RubikMedium16,
                color = VintrMusicExtendedTheme.colors.textRegular,
                maxLines = if (fixedHeight) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = artist,
                style = RubikRegular14,
                color = VintrMusicExtendedTheme.colors.textSecondary,
                maxLines = if (fixedHeight) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = durationText,
            style = RubikRegular14,
            color = VintrMusicExtendedTheme.colors.textRegular,
            maxLines = if (fixedHeight) 1 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
        )
        if (showTrailingAction) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingAction()
        }
    }
}
