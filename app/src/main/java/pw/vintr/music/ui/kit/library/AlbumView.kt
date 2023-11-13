package pw.vintr.music.ui.kit.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun AlbumView(
    modifier: Modifier = Modifier,
    album: AlbumModel,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .artworkContainer(RoundedCornerShape(4.dp)),
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.artworkUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = album.name,
            style = Gilroy16,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = buildString {
                append(album.artist.name)

                album.year?.let { year ->
                    append(String.Comma + String.Space)
                    append(year.toString())
                }
            },
            style = Gilroy14,
            color = VintrMusicExtendedTheme.colors.textSecondary
        )
    }
}
