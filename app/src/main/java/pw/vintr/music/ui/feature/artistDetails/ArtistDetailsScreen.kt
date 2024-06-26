package pw.vintr.music.ui.feature.artistDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.kit.button.ButtonBorderedIcon
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCells
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.toolbar.ToolbarWithArtwork
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.Gilroy36
import pw.vintr.music.ui.theme.Red2
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ArtistDetailsScreen(
    artist: ArtistModel,
    viewModel: ArtistDetailsViewModel = getViewModel {
        parametersOf(artist)
    }
) {
    val screenState = viewModel.screenState.collectAsState()
    val collapsingLayoutState = rememberCollapsingLayoutState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        ScreenStateLayout(
            state = screenState.value,
            toolbar = {
                ToolbarRegular(
                    title = String.Empty,
                    onBackPressed = { viewModel.navigateBack() },
                )
            },
            errorRetryAction = { viewModel.loadData() },
        ) { state ->
            val screenData = state.data

            CollapsingLayout(
                modifier = Modifier
                    .fillMaxSize(),
                state = collapsingLayoutState,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbar = {
                    ToolbarWithArtwork(
                        state = collapsingLayoutState,
                        artworkUrl = screenData.artist.artworkUrl,
                        mediaName = screenData.title,
                        titleSlot = {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .pin(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // Title
                                Text(
                                    modifier = Modifier
                                        .weight(1f),
                                    text = screenData.title,
                                    style = Gilroy36,
                                    color = VintrMusicExtendedTheme.colors.textRegular,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(modifier = Modifier.width(12.dp))

                                // Favorites button
                                ButtonBorderedIcon(
                                    iconRes = if (screenData.isFavorite) {
                                        R.drawable.ic_favorite_filled
                                    } else {
                                        R.drawable.ic_favorite_outline
                                    },
                                    tint = if (screenData.isFavorite) {
                                        Red2
                                    } else {
                                        VintrMusicExtendedTheme.colors.textRegular
                                    },
                                    onClick = {
                                        if (screenData.isFavorite) {
                                            viewModel.removeFromFavorites()
                                        } else {
                                            viewModel.addToFavorites()
                                        }
                                    }
                                )
                            }
                        },
                        onBackPressed = { viewModel.navigateBack() }
                    )
                },
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = rememberLibraryGridCells(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(
                        items = screenData.albums,
                        key = { it.id }
                    ) { album ->
                        AlbumView(
                            album = album,
                            onClick = { viewModel.onAlbumClick(album) }
                        )
                    }
                }
            }
        }
    }
}
