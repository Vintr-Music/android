package pw.vintr.music.ui.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.ArtistView
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.theme.Gilroy12
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val MENU_ITEM_LIBRARY = "item-library"
private const val MENU_ITEM_ALL_ARTISTS = "item-artists"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = getViewModel()
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val screenState = viewModel.screenState.collectAsState()

        ScreenStateLayout(
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() },
        ) { state ->
            val screenData = state.data
            val pullRefreshState = rememberPullRefreshState(
                refreshing = state.isRefreshing,
                onRefresh = { viewModel.refreshData() }
            )

            PullRefreshLayout(
                state = pullRefreshState,
                refreshing = state.isRefreshing
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    columns = GridCells.Fixed(count = 6),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Library title
                    item(
                        key = MENU_ITEM_LIBRARY,
                        span = { GridItemSpan(currentLineSpan = 6) }
                    ) {
                        MenuItemIconified(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            title = stringResource(id = R.string.library_personal),
                            titleStyle = RubikMedium16,
                            iconRes = R.drawable.ic_library,
                            iconSize = 20.dp,
                            iconTint = VintrMusicExtendedTheme.colors.textRegular
                        )
                    }

                    // Personal library header
                    items(
                        items = screenData.personalLibraryItems,
                        span = { GridItemSpan(currentLineSpan = 3) },
                        key = { it }
                    ) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                                .background(VintrMusicExtendedTheme.colors.cardBackground)
                                .clickable {
                                    when (item) {
                                        PersonalLibraryItem.ARTISTS -> {
                                            viewModel.openFavoriteArtists()
                                        }

                                        PersonalLibraryItem.ALBUMS -> {
                                            viewModel.openFavoriteAlbums()
                                        }

                                        PersonalLibraryItem.PLAYLISTS -> {
                                            viewModel.openPlaylists()
                                        }
                                    }
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = item.iconRes),
                                tint = VintrMusicExtendedTheme.colors.textRegular,
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(id = item.titleRes),
                                style = Gilroy14,
                                color = VintrMusicExtendedTheme.colors.textRegular,
                            )
                        }
                    }

                    // All artists header
                    item(
                        key = MENU_ITEM_ALL_ARTISTS,
                        span = { GridItemSpan(currentLineSpan = 6) }
                    ) {
                        MenuItemIconified(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.openAllArtists() }
                                .padding(vertical = 8.dp),
                            title = stringResource(id = R.string.all_artists),
                            titleStyle = RubikMedium16,
                            iconRes = R.drawable.ic_artist,
                            iconSize = 20.dp,
                            iconTint = VintrMusicExtendedTheme.colors.textRegular,
                            trailing = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.ic_forward),
                                    tint = VintrMusicExtendedTheme.colors.textRegular,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                    // All artists
                    items(
                        items = screenData.artists,
                        span = { GridItemSpan(currentLineSpan = 2) },
                        key = { artist -> artist.name }
                    ) { artist ->
                        ArtistView(
                            modifier = Modifier.padding(4.dp),
                            artist = artist,
                            textStyle = Gilroy12,
                            onClick = { viewModel.openArtist(artist) }
                        )
                    }
                }
            }
        }
    }
}
