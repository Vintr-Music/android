package pw.vintr.music.ui.kit.layout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import pw.vintr.music.domain.player.model.PlayerStateHolderModel
import pw.vintr.music.tools.composable.rememberSystemNavigationBarHeight
import pw.vintr.music.ui.feature.nowPlaying.NowPlayingScreen
import pw.vintr.music.ui.kit.dimen.DimensDp
import pw.vintr.music.ui.kit.player.BottomNowPlaying
import pw.vintr.music.ui.kit.sliding.BottomSheetScaffold
import pw.vintr.music.ui.kit.sliding.BottomSheetScaffoldState
import pw.vintr.music.ui.kit.sliding.rememberBottomSheetScaffoldState
import kotlin.math.max

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
    val navBarHeightDp = if (expandProgress > 0.3f) {
        0.dp
    } else {
        DimensDp.bottomNavigationHeight + bottomInset
    }
    val navBarHeightPx = with (density) { navBarHeightDp.toPx() }.toInt()

    // Nav bar animation
    val navBarAnimatedHeightDp = animateDpAsState(
        targetValue = navBarHeightDp,
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

    Layout(
        content = {
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
                    content(Modifier.padding(bottom = miniNowPlayingHeight.value))
                },
            )

            Layout(
                modifier = Modifier.clipToBounds(),
                content = { bottomNavigation(Modifier) },
                measurePolicy = measurePolicy
            )
        },
        modifier = modifier
    ) { measurables, constraints ->
        val navBarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )
        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = (constraints.maxHeight - navBarHeightPx).coerceAtLeast(0)
        )

        val navBarPlaceable = measurables[1].measure(navBarConstraints)

        val bodyMeasurables = measurables.subList(0, measurables.size - 1)

        val bodyPlaceableList = bodyMeasurables.map {
            it.measure(bodyConstraints)
        }

        val navBarHeightActual = navBarPlaceable.height

        val width = max(
            navBarPlaceable.width,
            bodyPlaceableList.maxOfOrNull { it.width } ?: 0
        ).coerceIn(constraints.minWidth, constraints.maxWidth)

        val height = max(
            navBarHeightActual,
            bodyPlaceableList.maxOfOrNull { it.height } ?: 0
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            bodyPlaceableList.forEach { placeable ->
                placeable.placeRelative(0, 0)
            }
            navBarPlaceable.placeRelative(0, height - navBarHeightActual)
        }
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
