package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel

@Composable
fun PlayerStatePager(
    modifier: Modifier = Modifier,
    state: PlayerStateHolderModel,
    onSeekToTrack: (Int) -> Unit,
    pageContent: @Composable (TrackModel) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentTrackIndex,
        pageCount = { state.session.tracks.size }
    )

    // Sync pager state with viewModel state when track changes programmatically
    LaunchedEffect(state.currentTrackIndex) {
        if (pagerState.currentPage != state.currentTrackIndex) {
            pagerState.animateScrollToPage(state.currentTrackIndex)
        }
    }

    // Handle user-initiated page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Only notify if the page change was due to user interaction
            if (pagerState.isScrollInProgress) {
                onSeekToTrack(page)
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        val track = state.session.tracks[page]

        pageContent(track)
    }
}
