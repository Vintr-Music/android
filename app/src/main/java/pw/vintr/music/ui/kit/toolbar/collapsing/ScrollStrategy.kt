package pw.vintr.music.ui.kit.toolbar.collapsing

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.roundToInt

interface CollapsingNestedScrollConnection : NestedScrollConnection {
    suspend fun snap() {}
}

enum class ScrollStrategy {
    EnterAlways {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior,
            snap: Boolean,
        ): CollapsingNestedScrollConnection =
            EnterAlwaysNestedScrollConnection(offsetY, toolbarState, flingBehavior)
    },
    EnterAlwaysCollapsed {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior,
            snap: Boolean,
        ): CollapsingNestedScrollConnection =
            EnterAlwaysCollapsedNestedScrollConnection(offsetY, toolbarState, flingBehavior)
    },
    ExitUntilCollapsed {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: CollapsingToolbarState,
            flingBehavior: FlingBehavior,
            snap: Boolean,
        ): CollapsingNestedScrollConnection =
            ExitUntilCollapsedNestedScrollConnection(toolbarState, flingBehavior, snap)
    };

    internal abstract fun create(
        offsetY: MutableState<Int>,
        toolbarState: CollapsingToolbarState,
        flingBehavior: FlingBehavior,
        snap: Boolean = false,
    ): CollapsingNestedScrollConnection
}

private class ScrollDelegate(
    private val offsetY: MutableState<Int>
) {
    private var scrollToBeConsumed: Float = 0f

    fun doScroll(delta: Float) {
        val scroll = scrollToBeConsumed + delta
        val scrollInt = scroll.toInt()

        scrollToBeConsumed = scroll - scrollInt

        offsetY.value += scrollInt
    }
}

internal class EnterAlwaysNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior
) : CollapsingNestedScrollConnection {
    private val scrollDelegate = ScrollDelegate(offsetY)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y

        val toolbar = toolbarState.height.toFloat()
        val offset = offsetY.value.toFloat()

        // -toolbarHeight <= offsetY + dy <= 0
        val consume = if (dy < 0) {
            val toolbarConsumption = toolbarState.dispatchRawDelta(dy)
            val remaining = dy - toolbarConsumption
            val offsetConsumption = remaining.coerceAtLeast(-toolbar - offset)
            scrollDelegate.doScroll(offsetConsumption)

            toolbarConsumption + offsetConsumption
        } else {
            val offsetConsumption = dy.coerceAtMost(-offset)
            scrollDelegate.doScroll(offsetConsumption)

            val toolbarConsumption = toolbarState.dispatchRawDelta(dy - offsetConsumption)

            offsetConsumption + toolbarConsumption
        }

        return Offset(0f, consume)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val left = if (available.y > 0) {
            toolbarState.fling(flingBehavior, available.y)
        } else {
            // If velocity < 0, the main content should have a remaining scroll space
            // so the scroll resumes to the onPreScroll(..., Fling) phase. Hence we do
            // not need to process it at onPostFling() manually.
            available.y
        }

        return Velocity(x = 0f, y = available.y - left)
    }
}

internal class EnterAlwaysCollapsedNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior
) : CollapsingNestedScrollConnection {
    private val scrollDelegate = ScrollDelegate(offsetY)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y

        val consumed = if (dy > 0) { // expanding: offset -> body -> toolbar
            val offsetConsumption = dy.coerceAtMost(-offsetY.value.toFloat())
            scrollDelegate.doScroll(offsetConsumption)

            offsetConsumption
        } else { // collapsing: toolbar -> offset -> body
            val toolbarConsumption = toolbarState.dispatchRawDelta(dy)
            val offsetConsumption = (dy - toolbarConsumption)
                .coerceAtLeast(-toolbarState.height.toFloat() - offsetY.value)

            scrollDelegate.doScroll(offsetConsumption)

            toolbarConsumption + offsetConsumption
        }

        return Offset(0f, consumed)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val dy = available.y

        return if (dy > 0) {
            Offset(0f, toolbarState.dispatchRawDelta(dy))
        } else {
            Offset(0f, 0f)
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        val dy = available.y

        val left = if (dy > 0) {
            // onPostFling() has positive available scroll value only called if the main scroll
            // has leftover scroll, i.e. the scroll of the main content has done. So we just process
            // fling if the available value is positive.
            toolbarState.fling(flingBehavior, dy)
        } else {
            dy
        }

        return Velocity(x = 0f, y = available.y - left)
    }
}

/**
 * Nested scroll for [ScrollStrategy.ExitUntilCollapsed].
 *
 * Mirrors Material3 / FsTopBar behaviour: drive collapse through a float [heightOffset] and,
 * while the offset is changing, consume the **entire** pre-scroll delta. That keeps finger
 * movement and body `placeRelative(toolbarHeight)` shift in sync, which is what prevents the
 * rubber-band bounce. Expand happens only in post-scroll once the list has leftover delta
 * (typically when it is already at the top).
 */
internal class ExitUntilCollapsedNestedScrollConnection(
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior,
    private val snap: Boolean,
) : CollapsingNestedScrollConnection {

    private var heightOffset = 0f
    private var contentOffset = 0f

    private val heightOffsetLimit: Float
        get() = (toolbarState.minHeight - toolbarState.maxHeight).toFloat()

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // ExitUntilCollapsed: never expand in pre-scroll.
        if (available.y > 0f) {
            return Offset.Zero
        }

        syncHeightOffsetFromToolbar()

        val previousOffset = heightOffset
        applyHeightOffset(heightOffset + available.y)

        return if (previousOffset != heightOffset) {
            // Consume all remaining delta while the bar is still collapsing so the list
            // does not move in the same frame as the body layout shift.
            available.copy(x = 0f)
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        syncHeightOffsetFromToolbar()
        contentOffset += consumed.y

        if (available.y < 0f || consumed.y < 0f) {
            val previousOffset = heightOffset
            applyHeightOffset(heightOffset + consumed.y)
            return Offset(0f, heightOffset - previousOffset)
        }

        if (consumed.y == 0f && available.y > 0f) {
            contentOffset = 0f
        }

        if (available.y > 0f) {
            val previousOffset = heightOffset
            applyHeightOffset(heightOffset + available.y)
            return Offset(0f, heightOffset - previousOffset)
        }

        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (available.y > 0f) {
            contentOffset = 0f
        }
        val remaining = settle(available.y)
        return Velocity(x = 0f, y = available.y - remaining)
    }

    override suspend fun snap() {
        settle(velocity = 0f)
    }

    private suspend fun settle(velocity: Float): Float {
        syncHeightOffsetFromToolbar()
        val progress = toolbarState.progress

        if (progress <= 0.01f) {
            return if (velocity > 1f) {
                val left = toolbarState.fling(flingBehavior, velocity)
                syncHeightOffsetFromToolbar()
                left
            } else {
                velocity
            }
        }

        if (progress >= 0.99f) {
            return velocity
        }

        var remainingVelocity = velocity
        if (abs(velocity) > 1f) {
            remainingVelocity = toolbarState.fling(flingBehavior, velocity)
            syncHeightOffsetFromToolbar()
        }

        if (snap) {
            val progressAfterFling = toolbarState.progress
            if (progressAfterFling > 0f && progressAfterFling < 1f) {
                if (progressAfterFling < 0.5f) {
                    toolbarState.collapse()
                } else {
                    toolbarState.expand()
                }
                syncHeightOffsetFromToolbar()
            }
        }

        return remainingVelocity
    }

    private fun syncHeightOffsetFromToolbar() {
        if (toolbarState.maxHeight == Int.MAX_VALUE ||
            toolbarState.maxHeight == toolbarState.minHeight
        ) {
            return
        }
        heightOffset = (toolbarState.height - toolbarState.maxHeight).toFloat()
            .coerceIn(heightOffsetLimit, 0f)
    }

    private fun applyHeightOffset(newOffset: Float) {
        val limit = heightOffsetLimit
        if (limit >= 0f) {
            // Toolbar has no collapse range yet (not measured / min == max).
            heightOffset = 0f
            return
        }

        heightOffset = newOffset.coerceIn(limit, 0f)
        toolbarState.setHeightFromOffset(heightOffset)
    }
}
