package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import kotlin.math.absoluteValue

@Composable
fun PlayerStatePager(
    modifier: Modifier = Modifier,
    pageSpacing: Dp = 0.dp,
    state: PlayerStateHolderModel,
    onSeekToTrack: (Int) -> Unit,
    pageContent: @Composable (TrackModel, Float) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentTrackIndex,
        pageCount = { state.session.tracks.size }
    )

    var isUserScroll by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress) {
            isUserScroll = true
        }
    }

    // Sync pager state with player state when track changes
    LaunchedEffect(state.currentTrackIndex) {
        val currentTrackIndex = state.currentTrackIndex
        val currentPage = pagerState.settledPage

        if (currentPage != currentTrackIndex) {
            isUserScroll = false
            pagerState.animateScrollToPage(state.currentTrackIndex)
        }
    }

    // Handle user-initiated page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            if (isUserScroll) {
                onSeekToTrack(page)
            }
        }
    }

    val absoluteOffset = pagerState.currentPageOffsetFraction.absoluteValue.coerceIn(0F, 0.5F)
    val openPercentage = 1 - (absoluteOffset * 2)

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageSpacing = pageSpacing,
    ) { page ->
        val track = state.session.tracks[page]

        pageContent(
            track,
            openPercentage,
        )
    }
}
