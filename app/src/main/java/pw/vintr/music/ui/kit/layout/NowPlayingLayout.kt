package pw.vintr.music.ui.kit.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.ui.feature.nowPlaying.NowPlayingScreen
import pw.vintr.music.ui.kit.dimen.DimensDp
import pw.vintr.music.ui.kit.player.BottomNowPlaying
import pw.vintr.music.ui.kit.sliding.BottomSheetScaffold
import pw.vintr.music.ui.kit.sliding.BottomSheetScaffoldState
import pw.vintr.music.ui.kit.sliding.rememberBottomSheetScaffoldState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingLayout(
    modifier: Modifier = Modifier,
    state: PlayerStateHolderModel,
    onControlClick: () -> Unit,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    bottomNavigation: @Composable (Modifier) -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val bottomInset = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(density = this).toDp()
    }
    val keyboardInset = with(LocalDensity.current) {
        WindowInsets.ime.getBottom(density = this).toDp()
    }

    val absoluteNavBarHeightDp = DimensDp.bottomNavigationHeight + bottomInset

    // Now playing dimens
    val showNowPlaying = state.currentTrack != null
    val miniNowPlayingHeight = animateDpAsState(
        targetValue = if (showNowPlaying) {
            DimensDp.nowMiniPlayingHeight + absoluteNavBarHeightDp
        } else {
            absoluteNavBarHeightDp
        },
        label = "Now playing collapsed height"
    )

    // Progress
    val boundedProgress = scaffoldState.bottomSheetState.progress
        .coerceIn(0f, 1f)
    val expandProgress = if (scaffoldState.bottomSheetState.isExpanded) {
        1 - boundedProgress
    } else {
        boundedProgress
    }
    val collapseProgress = (1 - expandProgress)

    Box(modifier = modifier.fillMaxSize()) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .alpha(expandProgress)
                            .fillMaxSize()
                    ) {
                        NowPlayingScreen()
                    }
                    BottomNowPlaying(
                        modifier = Modifier
                            .alpha(collapseProgress),
                        track = state.currentTrack,
                        playerStatus = state.status,
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onControlClick = onControlClick
                    )
                }
            },
            sheetBackgroundColor = MaterialTheme.colorScheme.background,
            backgroundColor = Color.Transparent,
            sheetElevation = 0.dp,
            sheetPeekHeight = miniNowPlayingHeight.value,
            content = {
                content(
                    Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = if (miniNowPlayingHeight.value > keyboardInset) {
                                miniNowPlayingHeight.value
                            } else {
                                keyboardInset
                            }
                        )
                )
            },
        )

        val hideNavBar = expandProgress > 0.2f

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = !hideNavBar,
        ) {
            bottomNavigation(Modifier)
        }
    }
}
