package pw.vintr.music.ui.kit.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ArtistView(
    modifier: Modifier = Modifier,
    artist: ArtistModel,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .artworkContainer(CircleShape),
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artist.artworkUrl)
                    .size(300)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = artist.name,
            style = Gilroy16,
            color = VintrMusicExtendedTheme.colors.textRegular,
            textAlign = TextAlign.Center
        )
    }
}
