package pw.vintr.music.ui.feature.albumDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.extension.pxToDpFloat
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.state.ScreenStateHolder
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun AlbumDetailsScreen(
    album: AlbumModel,
    viewModel: AlbumDetailsViewModel = getViewModel { parametersOf(album) }
) {
    val screenState = viewModel.screenState.collectAsState()
    val collapsingLayoutState = rememberCollapsingLayoutState()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        ScreenStateHolder(
            state = screenState.value,
            toolbar = {
                ToolbarRegular(
                    title = String.Empty,
                    onBackPressed = { viewModel.navigateBack() },
                )
            }
        ) { screenData ->
            CollapsingLayout(
                modifier = Modifier
                    .fillMaxSize(),
                state = collapsingLayoutState,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbar = {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(screenData.album.artworkUrl)
                                .size(Size.ORIGINAL)
                                .crossfade(enable = true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    collapsingLayoutState.toolbarState.height
                                        .pxToDpFloat().dp
                                        .coerceIn(
                                            minimumValue = 56.dp,
                                            maximumValue = constraints.maxHeight.dp
                                        )
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.background
                                                .copy(alpha = 0.0f),
                                            MaterialTheme.colorScheme.background
                                                .copy(alpha = 1.0f),
                                        )
                                    )
                                )
                        )
                    }

                    ToolbarRegular(
                        title = String.Empty,
                        backButtonColor = VintrMusicExtendedTheme.colors.textRegular,
                        onBackPressed = { viewModel.navigateBack() },
                    )
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = listState,
                ) {
                    items(screenData.tracks) {
                        TrackView(trackModel = it)
                    }
                }
            }
        }
    }
}
