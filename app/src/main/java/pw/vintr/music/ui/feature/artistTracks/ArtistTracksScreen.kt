package pw.vintr.music.ui.feature.artistTracks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.scaffoldPadding
import pw.vintr.music.tools.extension.shouldStartPaginate
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.pagination.paginationControls
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun ArtistTracksScreen(
    artist: ArtistModel,
    playingSessionId: String,
    viewModel: ArtistTracksViewModel = koinViewModel {
        parametersOf(artist, playingSessionId)
    }
) {
    val screenState = viewModel.screenState.collectAsState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(R.string.all_tracks),
                onBackPressed = { viewModel.navigateBack() },
            )
        }
    ) { scaffoldPadding ->
        ScreenStateLayout(
            modifier = Modifier
                .scaffoldPadding(scaffoldPadding),
            state = screenState.value,
            errorRetryAction = { viewModel.loadInitialData() },
        ) { state ->
            val playingState = state.data.playingState
            val hasNextPage = state.data.hasNextPage

            val shouldStartPaginate by remember(hasNextPage) {
                derivedStateOf {
                    lazyListState.shouldStartPaginate(
                        isLastPage = !hasNextPage
                    )
                }
            }

            LaunchedEffect(key1 = shouldStartPaginate) {
                if (shouldStartPaginate) {
                    viewModel.loadNextPage()
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .scaffoldPadding(scaffoldPadding),
                contentPadding = PaddingValues(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyListState,
            ) {
                // Header
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 20.dp)
                            .padding(top = 4.dp, bottom = 8.dp),
                    ) {
                        // Play-pause button
                        val isPlaying = playingState.playerStatus == PlayerStatusModel.PLAYING
                        val isLoading = playingState.playerStatus == PlayerStatusModel.LOADING

                        ButtonPlayerState(
                            modifier = Modifier
                                .align(Alignment.Center),
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
                itemsIndexed(
                    items = state.data.tracks,
                    key = { _, track -> track.md5 }
                ) { index, track ->
                    TrackView(
                        trackModel = track,
                        isPlaying = playingState.playingTrack == track,
                        onMoreClick = { viewModel.openTrackAction(track) },
                        onClick = { viewModel.playArtist(index) }
                    )
                }

                // Pagination-related controls
                paginationControls(
                    hasNextPage = state.data.hasNextPage,
                    hasFailedPage = state.data.hasFailedPage,
                    retryAction = { viewModel.loadNextPage() }
                )
            }
        }
    }
}
