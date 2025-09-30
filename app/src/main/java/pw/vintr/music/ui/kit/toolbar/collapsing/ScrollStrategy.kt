package pw.vintr.music.ui.kit.toolbar.collapsing

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

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

private enum class Direction {
    Idle,
    Expanding,
    Collapsing;
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
    //private val tracker = RelativeVelocityTracker(CurrentTimeProviderImpl())

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

internal class ExitUntilCollapsedNestedScrollConnection(
    private val toolbarState: CollapsingToolbarState,
    private val flingBehavior: FlingBehavior,
    private val snap: Boolean,
) : CollapsingNestedScrollConnection {
    private var direction: Direction = Direction.Idle

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y

        val consume = if (dy < 0) { // collapsing: toolbar -> body
            direction = Direction.Collapsing
            toolbarState.dispatchRawDelta(dy)
        } else {
            0f
        }

        return Offset(0f, consume)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val dy = available.y

        val consume = if (dy > 0) { // expanding: body -> toolbar
            direction = Direction.Expanding
            toolbarState.dispatchRawDelta(dy)
        } else {
            0f
        }

        return Offset(0f, consume)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val left = if (available.y < 0) {
            toolbarState.fling(flingBehavior, available.y)
        } else {
            available.y
        }

        return Velocity(x = 0f, y = available.y - left)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        val velocity = available.y

        val left = if (velocity > 0) {
            toolbarState.fling(flingBehavior, velocity)
        } else {
            velocity
        }

        snap()

        return Velocity(x = 0f, y = available.y - left)
    }

    override suspend fun snap() {
        val resultingToolbarProgress = toolbarState.progress

        if (snap && resultingToolbarProgress > 0 && resultingToolbarProgress < 1) {
            when (direction) {
                Direction.Expanding -> {
                    toolbarState.expand()
                }
                Direction.Collapsing -> {
                    toolbarState.collapse()
                }
                Direction.Idle -> Unit
            }
            direction = Direction.Idle
        }
    }
}
