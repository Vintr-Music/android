package pw.vintr.music.ui.feature.albumDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.kit.button.ButtonBorderedIcon
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.toolbar.ToolbarWithArtwork
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy36
import pw.vintr.music.ui.theme.Red2
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun AlbumDetailsScreen(
    album: AlbumModel,
    viewModel: AlbumDetailsViewModel = koinViewModel { parametersOf(album) }
) {
    val screenState = viewModel.screenState.collectAsState()
    val albumPlayingState = viewModel.albumPlayingState.collectAsState()

    val collapsingLayoutState = rememberCollapsingLayoutState()
    val listState = rememberLazyListState()

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
                        trailingSlot = {
                            ButtonSimpleIcon(
                                iconRes = R.drawable.ic_more,
                                onClick = { viewModel.openAlbumAction() },
                                tint = VintrMusicExtendedTheme.colors.textRegular,
                            )
                        },
                        onBackPressed = { viewModel.navigateBack() }
                    )
                },
            ) {
                val albumState = albumPlayingState.value

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
                            // Favorites button
                            ButtonBorderedIcon(
                                modifier = Modifier
                                    .padding(start = 76.dp)
                                    .align(Alignment.CenterStart)
                                    .alpha(collapsingLayoutState.toolbarState.progress),
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

                            // Play-pause button
                            val horizontalBias = -collapsingLayoutState.toolbarState.progress
                            val isPlaying = albumState.playerStatus == PlayerStatusModel.PLAYING
                            val isLoading = albumState.playerStatus == PlayerStatusModel.LOADING

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
                                    when (albumState.playerStatus) {
                                        PlayerStatusModel.IDLE -> {
                                            viewModel.playAlbum()
                                        }
                                        PlayerStatusModel.PAUSED -> {
                                            viewModel.resumeAlbum()
                                        }
                                        PlayerStatusModel.PLAYING -> {
                                            viewModel.pauseAlbum()
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }

                    itemsIndexed(
                        items = screenData.tracks,
                        key = { _, track -> track.md5 }
                    ) { index, track ->
                        TrackView(
                            trackModel = track,
                            isPlaying = albumState.playingTrack == track,
                            onMoreClick = { viewModel.openTrackAction(track) },
                            onClick = { viewModel.playAlbum(index) }
                        )
                    }
                }
            }
        }
    }
}
