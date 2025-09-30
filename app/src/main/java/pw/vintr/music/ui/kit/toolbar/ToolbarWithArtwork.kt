package pw.vintr.music.ui.kit.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import pw.vintr.music.tools.extension.pxToDpFloat
import pw.vintr.music.ui.kit.modifier.artworkGradientOverlayBackground
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayoutState
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingToolbarScope
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val BLUR_BASE_RADIUS_DP = 10

@Composable
fun CollapsingToolbarScope.ToolbarWithArtwork(
    state: CollapsingLayoutState,
    artworkUrl: String,
    mediaName: String,
    onBackPressed: () -> Unit,
    cachePolicy: CachePolicy? = null,
    trailingSlot: @Composable BoxScope.() -> Unit = {},
    titleSlot: @Composable CollapsingToolbarScope.() -> Unit = {},
) {
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp

    if (screenHeight > screenWidth) {
        val collapsePercentage = 1 - state.toolbarState.progress
        val blurAmount = (collapsePercentage * BLUR_BASE_RADIUS_DP).dp

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurAmount),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artworkUrl)
                    .size(Size.ORIGINAL)
                    .let { builder ->
                        cachePolicy?.let {
                            builder
                                .memoryCachePolicy(it)
                                .diskCachePolicy(it)
                        } ?: builder
                    }
                    .crossfade(enable = true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        state.toolbarState.height
                            .pxToDpFloat().dp
                            .coerceIn(
                                minimumValue = 56.dp,
                                maximumValue = constraints.maxHeight.dp
                            )
                    )
                    .artworkGradientOverlayBackground()
            )

            titleSlot()
        }

        ToolbarRegular(
            title = mediaName,
            titleOpacity = collapsePercentage,
            backButtonColor = VintrMusicExtendedTheme.colors.textRegular,
            trailing = trailingSlot,
            onBackPressed = { onBackPressed() },
        )
    } else {
        val height = with(LocalDensity.current) {
            56.dp + WindowInsets.statusBars.getTop(density = this).toDp()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(BLUR_BASE_RADIUS_DP.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artworkUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .artworkGradientOverlayBackground()
            )
        }

        ToolbarRegular(
            title = mediaName,
            backButtonColor = VintrMusicExtendedTheme.colors.textRegular,
            trailing = trailingSlot,
            onBackPressed = { onBackPressed() },
        )
    }
}
