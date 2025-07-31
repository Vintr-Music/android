package pw.vintr.music.ui.kit.toolbar.collapsing

import android.os.Bundle
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import kotlin.math.max

val LocalCollapsingScrollConnection = staticCompositionLocalOf<CollapsingNestedScrollConnection> {
    error("No CollapsingScrollConnection provided")
}

@Stable
class CollapsingLayoutState(
    val toolbarState: CollapsingToolbarState,
    initialOffsetY: Int = 0
) {
    val offsetY: Int
        get() = offsetYState.intValue

    internal val offsetYState = mutableIntStateOf(initialOffsetY)
}

private class CollapsingToolbarLayoutStateSaver: Saver<CollapsingLayoutState, Bundle> {
    override fun restore(value: Bundle): CollapsingLayoutState =
        CollapsingLayoutState(
            CollapsingToolbarState(
                initialHeight = value.getInt("height", Int.MAX_VALUE),
                initialMaxHeight = value.getInt("maxHeight", Int.MAX_VALUE),
                initialMinHeight = value.getInt("minHeight", 0)
            ),
            value.getInt("offsetY", 0)
        )

    override fun SaverScope.save(value: CollapsingLayoutState): Bundle =
        Bundle().apply {
            putInt("height", value.toolbarState.height)
            putInt("minHeight", value.toolbarState.minHeight)
            putInt("maxHeight", value.toolbarState.maxHeight)
            putInt("offsetY", value.offsetY)
        }
}

@Composable
fun rememberCollapsingLayoutState(
    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState()
): CollapsingLayoutState {
    return rememberSaveable(toolbarState, saver = CollapsingToolbarLayoutStateSaver()) {
        CollapsingLayoutState(toolbarState)
    }
}

interface CollapsingToolbarScaffoldScope {
    fun Modifier.align(alignment: Alignment): Modifier
}

@Composable
fun CollapsingLayout(
    modifier: Modifier,
    state: CollapsingLayoutState,
    scrollStrategy: ScrollStrategy,
    snap: Boolean = true,
    enabled: Boolean = true,
    toolbarModifier: Modifier = Modifier,
    toolbarClipToBounds: Boolean = true,
    toolbar: @Composable CollapsingToolbarScope.() -> Unit,
    body: @Composable CollapsingToolbarScaffoldScope.() -> Unit
) {
    val flingBehavior = ScrollableDefaults.flingBehavior()
    val layoutDirection = LocalLayoutDirection.current

    val nestedScrollConnection = remember(scrollStrategy, state, snap) {
        scrollStrategy.create(state.offsetYState, state.toolbarState, flingBehavior, snap)
    }

    val toolbarState = state.toolbarState

    CompositionLocalProvider(
        LocalCollapsingScrollConnection provides nestedScrollConnection
    ) {
        Layout(
            content = {
                CollapsingToolbar(
                    modifier = toolbarModifier,
                    clipToBounds = toolbarClipToBounds,
                    collapsingToolbarState = toolbarState,
                ) {
                    toolbar()
                }

                CollapsingToolbarScaffoldScopeInstance.body()
            },
            modifier = modifier
                .then(
                    if (enabled) {
                        Modifier.nestedScroll(nestedScrollConnection)
                    } else {
                        Modifier
                    }
                )
        ) { measurables, constraints ->
            check(measurables.size >= 2) {
                "the number of children should be at least 2: toolbar, (at least one) body"
            }

            val toolbarConstraints = constraints.copy(
                minWidth = 0,
                minHeight = 0
            )
            val bodyConstraints = constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxHeight = when (scrollStrategy) {
                    ScrollStrategy.ExitUntilCollapsed ->
                        (constraints.maxHeight - toolbarState.minHeight).coerceAtLeast(0)

                    ScrollStrategy.EnterAlways, ScrollStrategy.EnterAlwaysCollapsed ->
                        constraints.maxHeight
                }
            )

            val toolbarPlaceable = measurables[0].measure(toolbarConstraints)

            val bodyMeasurables = measurables.subList(1, measurables.size)
            val childrenAlignments = bodyMeasurables.map {
                (it.parentData as? ScaffoldParentData)?.alignment
            }
            val bodyPlaceableList = bodyMeasurables.map {
                it.measure(bodyConstraints)
            }

            val toolbarHeight = toolbarPlaceable.height

            val width = max(
                toolbarPlaceable.width,
                bodyPlaceableList.maxOfOrNull { it.width } ?: 0
            ).coerceIn(constraints.minWidth, constraints.maxWidth)

            val height = max(
                toolbarHeight,
                bodyPlaceableList.maxOfOrNull { it.height } ?: 0
            ).coerceIn(constraints.minHeight, constraints.maxHeight)

            layout(width, height) {
                bodyPlaceableList.forEachIndexed { index, placeable ->
                    val alignment = childrenAlignments[index]

                    if (alignment == null) {
                        placeable.placeRelative(0, toolbarHeight + state.offsetY)
                    } else {
                        val offset = alignment.align(
                            size = IntSize(placeable.width, placeable.height),
                            space = IntSize(width, height),
                            layoutDirection = layoutDirection
                        )
                        placeable.place(offset)
                    }
                }
                toolbarPlaceable.placeRelative(0, state.offsetY)
            }
        }
    }
}

internal object CollapsingToolbarScaffoldScopeInstance: CollapsingToolbarScaffoldScope {
    override fun Modifier.align(
        alignment: Alignment
    ): Modifier = this.then(ScaffoldChildAlignmentModifier(alignment))
}

private class ScaffoldChildAlignmentModifier(
    private val alignment: Alignment
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ScaffoldParentData) ?: ScaffoldParentData(alignment)
    }
}

private data class ScaffoldParentData(
    var alignment: Alignment? = null
)
