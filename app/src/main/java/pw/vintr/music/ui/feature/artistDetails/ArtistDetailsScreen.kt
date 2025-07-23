package pw.vintr.music.ui.feature.artistDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.extension.escapePadding
import pw.vintr.music.ui.kit.button.ButtonFavorite
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCells
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.FIXED_TRACK_VIEW_HEIGHT
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.kit.selector.PageIndicator
import pw.vintr.music.ui.kit.toolbar.ToolbarWithArtwork
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.Gilroy36
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val KEY_TRACKS_HEADER = "tracks-header"
private const val KEY_TRACKS = "tracks"

private const val KEY_ALBUMS_HEADER = "albums-header"

private const val TRACKS_ON_PAGE_COUNT = 3
private const val TRACKS_PAGE_COUNT = 4
private const val PREVIEW_TRACKS_COUNT = TRACKS_PAGE_COUNT * TRACKS_ON_PAGE_COUNT

@Composable
fun ArtistDetailsScreen(
    artist: ArtistModel,
    viewModel: ArtistDetailsViewModel = koinViewModel {
        parametersOf(artist)
    }
) {
    val screenState = viewModel.screenState.collectAsState()
    val artistPlayingState = viewModel.artistPlayingState.collectAsState()

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
            val tracksChunks = screenData.tracksPage.data
                .take(PREVIEW_TRACKS_COUNT)
                .chunked(TRACKS_ON_PAGE_COUNT)
            val pagerState = rememberPagerState(
                pageCount = { TRACKS_PAGE_COUNT }
            )

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
                            }
                        },
                        onBackPressed = { viewModel.navigateBack() }
                    )
                },
            ) {
                val playingState = artistPlayingState.value

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = rememberLibraryGridCells(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 8.dp, bottom = 4.dp),
                        ) {
                            // Favorites button
                            ButtonFavorite(
                                modifier = Modifier
                                    .padding(start = 76.dp)
                                    .align(Alignment.CenterStart)
                                    .alpha(collapsingLayoutState.toolbarState.progress),
                                isFavorite = screenData.isFavorite,
                            ) {
                                if (screenData.isFavorite) {
                                    viewModel.removeFromFavorites()
                                } else {
                                    viewModel.addToFavorites()
                                }
                            }

                            // Play-pause button
                            val horizontalBias = -collapsingLayoutState.toolbarState.progress
                            val isPlaying = playingState.playerStatus == PlayerStatusModel.PLAYING
                            val isLoading = playingState.playerStatus == PlayerStatusModel.LOADING

                            ButtonPlayerState(
                                modifier = Modifier.align(
                                    BiasAlignment(
                                        horizontalBias = horizontalBias,
                                        verticalBias = 1f
                                    )
                                ),
                                isPlaying = isPlaying,
                                isLoading = isLoading,
                                onClick = {
                                    when (playingState.playerStatus) {
                                        PlayerStatusModel.IDLE -> {
                                            viewModel.playArtist()
                                        }
                                        PlayerStatusModel.PAUSED -> {
                                            viewModel.resumeArtist()
                                        }
                                        PlayerStatusModel.PLAYING -> {
                                            viewModel.pauseArtist()
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }

                    // Tracks
                    item(
                        key = KEY_TRACKS_HEADER,
                        span = { GridItemSpan(currentLineSpan = 2) }
                    ) {
                        MenuItemIconified(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.openAllTracks() }
                                .padding(vertical = 8.dp),
                            title = stringResource(id = R.string.search_tracks_title),
                            iconRes = R.drawable.ic_track,
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
                    item(
                        key = KEY_TRACKS,
                        span = { GridItemSpan(currentLineSpan = 2)
                    }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            HorizontalPager(
                                modifier = Modifier
                                    .escapePadding(20.dp),
                                state = pagerState,
                                pageSpacing = 4.dp,
                            ) { page ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    val tracks = tracksChunks[page]

                                    tracks.forEachIndexed { index, track ->
                                        TrackView(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(FIXED_TRACK_VIEW_HEIGHT.dp),
                                            trackModel = track,
                                            fixedHeight = true,
                                            isPlaying = playingState.playingTrack == track,
                                            onMoreClick = { viewModel.openTrackAction(track) },
                                            onClick = {
                                                val trackIndex = (page + 1) * index
                                                viewModel.playArtist(trackIndex)
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            PageIndicator(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                pageCount = TRACKS_PAGE_COUNT,
                                selectedPageIndex = pagerState.currentPage,
                            )
                        }
                    }

                    // Albums
                    item(
                        key = KEY_ALBUMS_HEADER,
                        span = { GridItemSpan(currentLineSpan = 2) }
                    ) {
                        MenuItemIconified(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            title = stringResource(id = R.string.search_albums_title),
                            iconRes = R.drawable.ic_album,
                            iconSize = 20.dp,
                            iconTint = VintrMusicExtendedTheme.colors.textRegular
                        )
                    }
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
