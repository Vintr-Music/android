package pw.vintr.music.ui.kit.toolbar.collapsing

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

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
 * Mirrors Material3 / FsTopBar: float [heightOffset], consume-all while collapsing in
 * pre-scroll, expand only from post-scroll leftover.
 *
 * Bounce fix: after a short downward flick the list often starts a SideEffect fling *up*
 * (inverted leftover velocity). That reverse motion used to rubber-band the content and
 * then open the toolbar. See [shouldBlockRebound], [onPreFling], [onPreScroll], and
 * [onPostScroll]. The guard must stay limited to downward-only near-top flicks — once
 * the user has scrolled up or gone past [reboundGuardDistance], rebound handling is off
 * so a fling back to the top is not inverted/stalled.
 */
internal class ExitUntilCollapsedNestedScrollConnection(
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior,
    private val snap: Boolean,
) : CollapsingNestedScrollConnection {

    private var heightOffset = 0f
    private var contentOffset = 0f

    /**
     * Sign of the last user drag: -1 = scrolling content down / collapsing,
     * +1 = scrolling toward top / expanding.
     */
    private var lastUserInputSign = 0

    /**
     * True once this gesture has had any upward UserInput. Prevents rebound redirect
     * after "scroll down, then back up" — a trailing down micro-delta near the top
     * must not invert the upward fling.
     */
    private var hadUpwardUserScroll = false

    /**
     * True once content has scrolled past [reboundGuardDistance] in this gesture.
     * Survives a wrong contentOffset reset so deep-list flings stay unaffected.
     */
    private var scrolledPastReboundGuard = false

    /**
     * Only guard rebound while still near the top after a short downward flick.
     */
    private val reboundGuardDistance = 300f

    private val heightOffsetLimit: Float
        get() = (toolbarState.minHeight - toolbarState.maxHeight).toFloat()

    /**
     * Rebound = upward leftover after a downward-only near-top gesture.
     * Not armed after upward user scroll or after scrolling deeper into the list.
     */
    private fun shouldBlockRebound(availableY: Float): Boolean =
        lastUserInputSign < 0 &&
            availableY > 0f &&
            !hadUpwardUserScroll &&
            !scrolledPastReboundGuard &&
            contentOffset > -reboundGuardDistance

    private fun resetReboundGestureState() {
        lastUserInputSign = 0
        hadUpwardUserScroll = false
        scrolledPastReboundGuard = false
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        // Child fling velocity = available - consumed. Consuming 2× an upward
        // rebound leaves the list with −available (downward) so the flick continues
        // in the user-intended direction instead of bouncing back or stalling.
        if (shouldBlockRebound(available.y)) {
            return Velocity(x = 0f, y = available.y * 2f)
        }
        return Velocity.Zero
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y > 0f) {
            // Residual upward SideEffect after a downward gesture (preFling miss /
            // overscroll). Eat before the list applies it so content does not
            // rubber-band back toward the top.
            if (
                source != NestedScrollSource.UserInput &&
                shouldBlockRebound(available.y)
            ) {
                return available.copy(x = 0f)
            }
            // ExitUntilCollapsed: never expand in pre-scroll.
            return Offset.Zero
        }

        if (source == NestedScrollSource.UserInput) {
            lastUserInputSign = -1
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
        if (contentOffset <= -reboundGuardDistance) {
            scrolledPastReboundGuard = true
        }

        if (source == NestedScrollSource.UserInput) {
            when {
                available.y > 0f || consumed.y > 0f -> {
                    lastUserInputSign = 1
                    hadUpwardUserScroll = true
                }
                consumed.y < 0f || available.y < 0f -> lastUserInputSign = -1
            }
        }

        return if (available.y < 0f || consumed.y < 0f) {
            val previousOffset = heightOffset
            applyHeightOffset(heightOffset + consumed.y)
            Offset(0f, heightOffset - previousOffset)
        } else {
            if (consumed.y == 0f && available.y > 0f) {
                contentOffset = 0f
            }

            if (available.y > 0f) {
                // List fling reversed after a downward-only near-top gesture and hit
                // the top. Do not open the toolbar; eat leftover so parents don't either.
                if (
                    source != NestedScrollSource.UserInput &&
                    shouldBlockRebound(available.y)
                ) {
                    Offset(0f, available.y)
                } else {
                    val previousOffset = heightOffset
                    applyHeightOffset(heightOffset + available.y)
                    Offset(0f, heightOffset - previousOffset)
                }
            } else {
                Offset.Zero
            }
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (available.y > 0f) {
            contentOffset = 0f
        }
        val remaining = settle(available.y)
        resetReboundGestureState()
        return Velocity(x = 0f, y = available.y - remaining)
    }

    override suspend fun snap() {
        settle(velocity = 0f)
        resetReboundGestureState()
    }

    private suspend fun settle(velocity: Float): Float {
        syncHeightOffsetFromToolbar()
        val progress = toolbarState.progress

        // Material3 settleAppBar: already collapsed/expanded — do nothing.
        // Avoids fling-expand on a collapsed bar after a downward gesture (bounce).
        if (progress <= 0.01f || progress >= 0.99f) {
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
                // Snap to nearest endpoint (Material3-style), not by fling direction.
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
