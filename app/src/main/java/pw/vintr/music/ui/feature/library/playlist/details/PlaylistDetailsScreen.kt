package pw.vintr.music.ui.feature.library.playlist.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.request.CachePolicy
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.state.EmptyState
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.kit.toolbar.ToolbarWithArtwork
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy36
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailsScreen(
    playlistId: String,
    viewModel: PlaylistDetailsViewModel = getViewModel { parametersOf(playlistId) }
) {
    val screenState = viewModel.screenState.collectAsState()
    val playlistPlayingState = viewModel.playlistPlayingState.collectAsState()

    val collapsingLayoutState = rememberCollapsingLayoutState()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
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
            empty = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ToolbarRegular(
                        title = String.Empty,
                        onBackPressed = { viewModel.navigateBack() },
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth(),
                            iconRes = R.drawable.ic_empty_playlist,
                            text = stringResource(id = R.string.playlist_tracks_empty),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
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
                        artworkUrl = screenData.playlist.artworkUrl,
                        mediaName = screenData.title,
                        cachePolicy = CachePolicy.DISABLED,
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
                                if (screenData.subtitle.isNotEmpty()) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = screenData.subtitle,
                                        style = Gilroy16,
                                        color = VintrMusicExtendedTheme.colors.textRegular,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        },
                        trailingSlot = {
                            ButtonSimpleIcon(
                                iconRes = R.drawable.ic_more,
                                onClick = { viewModel.openPlaylistAction() },
                                tint = VintrMusicExtendedTheme.colors.textRegular,
                            )
                        },
                        onBackPressed = { viewModel.navigateBack() }
                    )
                },
            ) {
                val playingState = playlistPlayingState.value

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
                                            viewModel.playPlaylist()
                                        }
                                        PlayerStatusModel.PAUSED -> {
                                            viewModel.resumePlaylist()
                                        }
                                        PlayerStatusModel.PLAYING -> {
                                            viewModel.pausePlaylist()
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }

                    itemsIndexed(
                        items = screenData.records,
                        key = { _, record -> record.id }
                    ) { index, record ->
                        TrackView(
                            trackModel = record.track,
                            isPlaying = playingState.playingTrack == record.track,
                            onMoreClick = { viewModel.openTrackAction(record) },
                            onClick = { viewModel.playPlaylist(index) }
                        )
                    }
                }
            }
        }
    }
}
