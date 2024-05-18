package pw.vintr.music.tools.composable

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

class BottomSheetNestedScrollInterceptor(
    private val lazyListState: LazyListState,
    private val allowOverscroll: Boolean,
) : NestedScrollConnection {

    private var lastPreScrollSource: NestedScrollSource = NestedScrollSource.Fling

    private var arrivedBoundarySource: NestedScrollSource? = null

    private var canPassScrollEvent: Boolean = true

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // Reset the state variable
        if (
            source == NestedScrollSource.Drag &&
            arrivedBoundarySource == NestedScrollSource.Fling
        ) {
            arrivedBoundarySource = null
        }

        if (
            source == NestedScrollSource.Drag &&
            lastPreScrollSource == NestedScrollSource.Fling
        ) {
            canPassScrollEvent = lazyListState.firstVisibleItemIndex == 0 &&
                    !lazyListState.canScrollBackward
        }

        lastPreScrollSource = source

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // The sub-layout can't consume completely,
        // which means that the boundary has been reached.
        if (arrivedBoundarySource == null && abs(available.y) > 0) {
            arrivedBoundarySource = source
        }

        // Decide whether to consume according to the sub-layout
        // consumption when reaching the boundary.
        if (arrivedBoundarySource == NestedScrollSource.Fling) {
            return if (allowOverscroll) {
                Offset.Zero
            } else {
                available
            }
        }

        return if (canPassScrollEvent) {
            Offset.Zero
        } else {
            available
        }
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        arrivedBoundarySource = null
        return super.onPostFling(consumed, available)
    }
}

@Composable
fun rememberBottomSheetNestedScrollInterceptor(
    lazyListState: LazyListState,
    allowOverscroll: Boolean = true,
): BottomSheetNestedScrollInterceptor {
    return remember { BottomSheetNestedScrollInterceptor(lazyListState, allowOverscroll) }
}
