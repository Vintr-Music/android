package pw.vintr.music.ui.feature.albumDetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.state.ScreenStateHolder
import pw.vintr.music.ui.kit.toolbar.ToolbarArtwork
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy36
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalFoundationApi::class)
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
                    ToolbarArtwork(
                        state = collapsingLayoutState,
                        artworkUrl = screenData.album.artworkUrl,
                        mediaName = screenData.title,
                        titleSlot = {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 20.dp)
                                    .pin(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = screenData.title,
                                    style = Gilroy36,
                                    color = VintrMusicExtendedTheme.colors.textRegular,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = screenData.subtitle,
                                    style = Gilroy16,
                                    color = VintrMusicExtendedTheme.colors.textRegular,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        },
                        onBackPressed = { viewModel.navigateBack() }
                    )
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = listState,
                ) {
                    stickyHeader { 
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp, bottom = 4.dp),
                        ) {
                            val horizontalBias = -collapsingLayoutState.toolbarState.progress

                            ButtonPlayerState(
                                modifier = Modifier.align(
                                    BiasAlignment(
                                        horizontalBias = horizontalBias,
                                        verticalBias = 0f
                                    )
                                )
                            )
                        }
                    }

                    items(screenData.tracks) {
                        TrackView(trackModel = it)
                    }
                }
            }
        }
    }
}
