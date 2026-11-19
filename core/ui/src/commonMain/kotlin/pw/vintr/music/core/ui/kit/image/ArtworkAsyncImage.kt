package pw.vintr.music.core.ui.kit.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size

@Composable
fun ArtworkAsyncImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    size: Size? = Size(300, 300),
    cachePolicy: CachePolicy? = null,
    crossfade: Boolean = true,
    error: Painter? = null,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .let { builder ->
                var next = builder
                if (size != null) {
                    next = next.size(size)
                }
                if (crossfade) {
                    next = next.crossfade(enable = true)
                }
                if (cachePolicy != null) {
                    next = next
                        .memoryCachePolicy(cachePolicy)
                        .diskCachePolicy(cachePolicy)
                }
                next
            }
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        error = error,
    )
}
