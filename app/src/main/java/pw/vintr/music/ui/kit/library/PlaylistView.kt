package pw.vintr.music.ui.kit.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pw.vintr.music.R
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun PlaylistView(
    modifier: Modifier = Modifier,
    playlist: PlaylistModel,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .artworkContainer(RoundedCornerShape(4.dp)),
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.artworkUrl)
                    .size(300)
                    .crossfade(enable = true)
                    .error(drawableResId = R.drawable.ic_playlist_no_artwork)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = playlist.name,
            style = Gilroy16,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
    }
}
