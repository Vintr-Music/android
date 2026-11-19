package pw.vintr.music.core.ui.kit.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.request.CachePolicy
import pw.vintr.music.core.ui.kit.image.ArtworkAsyncImage
import pw.vintr.music.core.ui.kit.image.playlistNoArtworkPainter
import pw.vintr.music.core.ui.kit.modifier.artworkContainer
import pw.vintr.music.core.ui.theme.Gilroy16
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

@Composable
fun PlaylistView(
    modifier: Modifier = Modifier,
    artworkUrl: String?,
    name: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.artworkContainer(RoundedCornerShape(4.dp)),
        ) {
            ArtworkAsyncImage(
                modifier = Modifier.fillMaxSize(),
                url = artworkUrl,
                cachePolicy = CachePolicy.DISABLED,
                error = playlistNoArtworkPainter(),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            style = Gilroy16,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
    }
}
