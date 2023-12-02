package pw.vintr.music.ui.kit.layout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.tools.composable.rememberSystemNavigationBarHeight
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
    val bottomInset = rememberSystemNavigationBarHeight()
    val density = LocalDensity.current

    // Now playing dimens
    val showNowPlaying = state.currentTrack != null
    val miniNowPlayingHeight = animateDpAsState(
        targetValue = if (showNowPlaying) DimensDp.nowMiniPlayingHeight else 0.dp,
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

    // Nav bar dimens
    val absoluteNavBarHeightDp = DimensDp.bottomNavigationHeight + bottomInset

    val actualNavBarHeightDp = if (expandProgress > 0.3f) {
        0.dp
    } else {
        absoluteNavBarHeightDp
    }

    // Nav bar animation
    val navBarAnimatedHeightDp = animateDpAsState(
        targetValue = actualNavBarHeightDp,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        ),
        label = "Navigation bar height"
    )
    val navBarAnimatedHeightPx = with (density) { navBarAnimatedHeightDp.value.toPx() }.toInt()

    val measurePolicy = remember(navBarAnimatedHeightPx) {
        CollapsingNavbarMeasurePolicy(navBarAnimatedHeightPx)
    }

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
            sheetBottomPadding = actualNavBarHeightDp,
            content = {
                content(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = absoluteNavBarHeightDp)
                        .padding(bottom = miniNowPlayingHeight.value)
                )
            },
        )
        Layout(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clipToBounds(),
            content = { bottomNavigation(Modifier) },
            measurePolicy = measurePolicy
        )
    }
}

private class CollapsingNavbarMeasurePolicy(private val height: Int): MeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val placeableList = measurables.map {
            it.measure(
                constraints.copy(
                    minWidth = 0,
                    minHeight = 0,
                    maxHeight = Constraints.Infinity
                )
            )
        }

        val maxWidth = placeableList.maxOfOrNull{ it.width }
            ?.coerceIn(constraints.minWidth, constraints.maxWidth) ?: 0

        return layout(maxWidth, height) {
            placeableList.forEach { placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }
}
