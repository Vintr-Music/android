package pw.vintr.music.ui.kit.sliding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import kotlin.math.abs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

/**
 * Possible values of [BottomSheetState].
 */
@ExperimentalMaterialApi
enum class BottomSheetValue {
    /**
     * The bottom sheet is visible, but only showing its peek height.
     */
    Collapsed,

    /**
     * The bottom sheet is visible at its maximum height.
     */
    Expanded
}

@Deprecated(
    message = "This constructor is deprecated. confirmStateChange has been renamed to " +
            "confirmValueChange.",
    replaceWith = ReplaceWith(
        "BottomSheetScaffoldState(initialValue, animationSpec, " +
                "confirmStateChange)"
    )
)
@Suppress("Deprecation", "FunctionName")
@ExperimentalMaterialApi
fun BottomSheetScaffoldState(
    initialValue: BottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BottomSheetValue) -> Boolean
) = BottomSheetState(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmValueChange = confirmStateChange
)

/**
 * State of the persistent bottom sheet in [BottomSheetScaffold].
 *
 * @param initialValue The initial value of the state.
 * @param density The density that this state can use to convert values to and from dp.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@Suppress("Deprecation")
@ExperimentalMaterialApi
@Stable
fun BottomSheetState(
    initialValue: BottomSheetValue,
    density: Density,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmValueChange: (BottomSheetValue) -> Boolean = { true }
) = BottomSheetState(initialValue, animationSpec, confirmValueChange).also {
    it.density = density
}

/**
 * State of the persistent bottom sheet in [BottomSheetScaffold].
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@ExperimentalMaterialApi
@Stable
class BottomSheetState @Deprecated(
    "This constructor is deprecated. Density must be provided by the component. " +
            "Please use the constructor that provides a [Density].",
    ReplaceWith(
        """
            BottomSheetState(
                initialValue = initialValue,
                density = LocalDensity.current,
                animationSpec = animationSpec,
                confirmValueChange = confirmValueChange
            )
            """
    )
) constructor(
    initialValue: BottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmValueChange: (BottomSheetValue) -> Boolean = { true }
) {

    internal val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = animationSpec,
        confirmValueChange = confirmValueChange,
        positionalThreshold = {
            with(requireDensity()) {
                BottomSheetScaffoldPositionalThreshold.toPx()
            }
        },
        velocityThreshold = {
            with(requireDensity()) {
                BottomSheetScaffoldVelocityThreshold.toPx()
            }
        }
    )

    /**
     * The current value of the [BottomSheetState].
     */
    val currentValue: BottomSheetValue
        get() = anchoredDraggableState.currentValue

    /**
     * The target value the state will settle at once the current interaction ends, or the
     * [currentValue] if there is no interaction in progress.
     */
    @Suppress("Unused")
    val targetValue: BottomSheetValue
        get() = anchoredDraggableState.targetValue

    /**
     * Whether the bottom sheet is expanded.
     */
    val isExpanded: Boolean
        get() = anchoredDraggableState.currentValue == BottomSheetValue.Expanded

    /**
     * Whether the bottom sheet is collapsed.
     */
    val isCollapsed: Boolean
        get() = anchoredDraggableState.currentValue == BottomSheetValue.Collapsed

    /**
     * The fraction of the progress, within [0f..1f] bounds, or 1f if the [AnchoredDraggableState]
     * is in a settled state.
     */
    /*@FloatRange(from = 0f, to = 1f)*/
    @ExperimentalMaterialApi
    val progress: Float
        get() = anchoredDraggableState.progress

    /**
     * Expand the bottom sheet with an animation and suspend until the animation finishes or is
     * cancelled.
     * Note: If the peek height is equal to the sheet height, this method will animate to the
     * [BottomSheetValue.Collapsed] state.
     *
     * This method will throw [CancellationException] if the animation is interrupted.
     */
    suspend fun expand() {
        val target = if (anchoredDraggableState.hasAnchorForValue(BottomSheetValue.Expanded)) {
            BottomSheetValue.Expanded
        } else {
            BottomSheetValue.Collapsed
        }
        anchoredDraggableState.animateTo(target)
    }

    /**
     * Collapse the bottom sheet with animation and suspend until it if fully collapsed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted.
     */
    suspend fun collapse() = anchoredDraggableState.animateTo(BottomSheetValue.Collapsed)

    /**
     * Require the current offset.
     *
     * @throws IllegalStateException If the offset has not been initialized yet
     */
    fun requireOffset() = anchoredDraggableState.requireOffset()

    internal suspend fun animateTo(
        target: BottomSheetValue,
        velocity: Float = anchoredDraggableState.lastVelocity
    ) = anchoredDraggableState.animateTo(target, velocity)

    internal suspend fun snapTo(target: BottomSheetValue) = anchoredDraggableState.snapTo(target)

    internal fun trySnapTo(target: BottomSheetValue) = anchoredDraggableState.trySnapTo(target)

    val isAnimationRunning: Boolean get() = anchoredDraggableState.isAnimationRunning

    internal var density: Density? = null
    private fun requireDensity() = requireNotNull(density) {
        "The density on BottomSheetState ($this) was not set. Did you use BottomSheetState with " +
                "the BottomSheetScaffold composable?"
    }

    internal val lastVelocity: Float get() = anchoredDraggableState.lastVelocity

    companion object {

        /**
         * The default [Saver] implementation for [BottomSheetState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BottomSheetValue) -> Boolean,
            density: Density
        ): Saver<BottomSheetState, *> = Saver(
            save = { it.anchoredDraggableState.currentValue },
            restore = {
                BottomSheetState(
                    initialValue = it,
                    density = density,
                    animationSpec = animationSpec,
                    confirmValueChange = confirmStateChange
                )
            }
        )

        /**
         * The default [Saver] implementation for [BottomSheetState].
         */
        @Deprecated(
            message = "This function is deprecated. Please use the overload where Density is" +
                    " provided.",
            replaceWith = ReplaceWith(
                "Saver(animationSpec, confirmStateChange, density)"
            )
        )
        @Suppress("Deprecation")
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BottomSheetValue) -> Boolean
        ): Saver<BottomSheetState, *> = Saver(
            save = { it.anchoredDraggableState.currentValue },
            restore = {
                BottomSheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmValueChange = confirmStateChange
                )
            }
        )
    }
}

/**
 * Create a [BottomSheetState] and [remember] it.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
@ExperimentalMaterialApi
fun rememberBottomSheetState(
    initialValue: BottomSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BottomSheetValue) -> Boolean = { true }
): BottomSheetState {
    val density = LocalDensity.current
    return rememberSaveable(
        animationSpec,
        saver = BottomSheetState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            density = density
        )
    ) {
        BottomSheetState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmValueChange = confirmStateChange,
            density = density
        )
    }
}

/**
 * State of the [BottomSheetScaffold] composable.
 *
 * @param drawerState The state of the navigation drawer.
 * @param bottomSheetState The state of the persistent bottom sheet.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@ExperimentalMaterialApi
@Stable
class BottomSheetScaffoldState(
    val drawerState: DrawerState,
    val bottomSheetState: BottomSheetState,
    val snackbarHostState: SnackbarHostState
)

/**
 * Create and [remember] a [BottomSheetScaffoldState].
 *
 * @param drawerState The state of the navigation drawer.
 * @param bottomSheetState The state of the persistent bottom sheet.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@Composable
@ExperimentalMaterialApi
fun rememberBottomSheetScaffoldState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    bottomSheetState: BottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): BottomSheetScaffoldState {
    return remember(drawerState, bottomSheetState, snackbarHostState) {
        BottomSheetScaffoldState(
            drawerState = drawerState,
            bottomSheetState = bottomSheetState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun BottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    topBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    sheetGesturesEnabled: Boolean = true,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = BottomSheetScaffoldDefaults.SheetElevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    sheetPeekHeight: Dp = BottomSheetScaffoldDefaults.SheetPeekHeight,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (PaddingValues) -> Unit
) {
    // b/278692145 Remove this once deprecated methods without density are removed
    if (scaffoldState.bottomSheetState.density == null) {
        val density = LocalDensity.current
        SideEffect {
            scaffoldState.bottomSheetState.density = density
        }
    }

    val peekHeightPx = with(LocalDensity.current) { sheetPeekHeight.toPx() }
    val child = @Composable {
        BottomSheetScaffoldLayout(
            topBar = topBar,
            body = content,
            bottomSheet = { layoutHeight ->
                val nestedScroll = if (sheetGesturesEnabled) {
                    Modifier
                        .nestedScroll(
                            remember(scaffoldState.bottomSheetState.anchoredDraggableState) {
                                ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
                                    state = scaffoldState.bottomSheetState.anchoredDraggableState,
                                    orientation = Orientation.Vertical
                                )
                            }
                        )
                } else Modifier
                BottomSheet(
                    state = scaffoldState.bottomSheetState,
                    modifier = nestedScroll
                        .fillMaxWidth()
                        .requiredHeightIn(min = sheetPeekHeight),
                    calculateAnchors = { sheetSize ->
                        val sheetHeight = sheetSize.height.toFloat()
                        val collapsedHeight = layoutHeight - peekHeightPx
                        if (sheetHeight == 0f || sheetHeight == peekHeightPx) {
                            mapOf(BottomSheetValue.Collapsed to collapsedHeight)
                        } else {
                            mapOf(
                                BottomSheetValue.Collapsed to collapsedHeight,
                                BottomSheetValue.Expanded to layoutHeight - sheetHeight
                            )
                        }
                    },
                    sheetBackgroundColor = sheetBackgroundColor,
                    sheetContentColor = sheetContentColor,
                    sheetElevation = sheetElevation,
                    sheetGesturesEnabled = sheetGesturesEnabled,
                    sheetShape = sheetShape,
                    content = sheetContent
                )
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHost(scaffoldState.snackbarHostState)
            },
            sheetOffset = { scaffoldState.bottomSheetState.requireOffset() },
            sheetPeekHeight = sheetPeekHeight,
            sheetState = scaffoldState.bottomSheetState,
            floatingActionButtonPosition = floatingActionButtonPosition
        )
    }
    Surface(
        modifier
            .fillMaxSize(),
        color = backgroundColor,
        contentColor = contentColor
    ) {
        if (drawerContent == null) {
            child()
        } else {
            ModalDrawer(
                drawerContent = drawerContent,
                drawerState = scaffoldState.drawerState,
                gesturesEnabled = drawerGesturesEnabled,
                drawerShape = drawerShape,
                drawerElevation = drawerElevation,
                drawerBackgroundColor = drawerBackgroundColor,
                drawerContentColor = drawerContentColor,
                scrimColor = drawerScrimColor,
                content = child
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheet(
    state: BottomSheetState,
    sheetGesturesEnabled: Boolean,
    calculateAnchors: (sheetSize: IntSize) -> Map<BottomSheetValue, Float>,
    sheetShape: Shape,
    sheetElevation: Dp,
    sheetBackgroundColor: Color,
    sheetContentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val anchorChangeCallback = remember(state, scope) {
        BottomSheetScaffoldAnchorChangeCallback(state, scope)
    }
    Surface(
        modifier
            .anchoredDraggable(
                state = state.anchoredDraggableState,
                orientation = Orientation.Vertical,
                enabled = sheetGesturesEnabled,
            )
            .onSizeChanged { layoutSize ->
                state.anchoredDraggableState.updateAnchors(
                    newAnchors = calculateAnchors(layoutSize),
                    onAnchorsChanged = anchorChangeCallback
                )
            }
            .semantics {
                // If we don't have anchors yet, or have only one anchor we don't want any
                // accessibility actions
                if (state.anchoredDraggableState.anchors.size > 1) {
                    if (state.isCollapsed) {
                        expand {
                            if (state.anchoredDraggableState.confirmValueChange(BottomSheetValue.Expanded)) {
                                scope.launch { state.expand() }
                            }
                            true
                        }
                    } else {
                        collapse {
                            if (state.anchoredDraggableState.confirmValueChange(BottomSheetValue.Collapsed)) {
                                scope.launch { state.collapse() }
                            }
                            true
                        }
                    }
                }
            },
        shape = sheetShape,
        elevation = sheetElevation,
        color = sheetBackgroundColor,
        contentColor = sheetContentColor,
        content = { Column(content = content) }
    )
}

/**
 * Contains useful defaults for [BottomSheetScaffold].
 */
object BottomSheetScaffoldDefaults {
    /**
     * The default elevation used by [BottomSheetScaffold].
     */
    val SheetElevation = 8.dp

    /**
     * The default peek height used by [BottomSheetScaffold].
     */
    val SheetPeekHeight = 56.dp
}

private enum class BottomSheetScaffoldLayoutSlot { TopBar, Body, Sheet, Fab, Snackbar }

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetScaffoldLayout(
    topBar: @Composable (() -> Unit)?,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    bottomSheet: @Composable (layoutHeight: Int) -> Unit,
    floatingActionButton: (@Composable () -> Unit)?,
    snackbarHost: @Composable () -> Unit,
    sheetPeekHeight: Dp,
    floatingActionButtonPosition: FabPosition,
    sheetOffset: () -> Float,
    sheetState: BottomSheetState,
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val sheetPlaceables = subcompose(BottomSheetScaffoldLayoutSlot.Sheet) {
            bottomSheet(layoutHeight)
        }.map { it.measure(looseConstraints) }
        val sheetOffsetY = sheetOffset().roundToInt()

        val topBarPlaceables = topBar?.let {
            subcompose(BottomSheetScaffoldLayoutSlot.TopBar, topBar)
                .map { it.measure(looseConstraints) }
        }
        val topBarHeight = topBarPlaceables?.maxBy { it.height }?.height ?: 0

        val bodyConstraints = looseConstraints.copy(maxHeight = layoutHeight - topBarHeight)
        val bodyPlaceables = subcompose(BottomSheetScaffoldLayoutSlot.Body) {
            body(PaddingValues(bottom = sheetPeekHeight))
        }.map { it.measure(bodyConstraints) }

        val fabPlaceable = floatingActionButton?.let { fab ->
            subcompose(BottomSheetScaffoldLayoutSlot.Fab, fab).map { it.measure(looseConstraints) }
        }
        val fabWidth = fabPlaceable?.maxBy { it.width }?.width ?: 0
        val fabHeight = fabPlaceable?.maxBy { it.height }?.height ?: 0
        val fabOffsetX = when (floatingActionButtonPosition) {
            FabPosition.Center -> (layoutWidth - fabWidth) / 2
            else -> layoutWidth - fabWidth - FabSpacing.roundToPx()
        }
        // In case sheet peek height < (FAB height / 2), give the FAB some minimum space
        val fabOffsetY = if (sheetPeekHeight.toPx() < fabHeight / 2) {
            sheetOffsetY - fabHeight - FabSpacing.roundToPx()
        } else sheetOffsetY - (fabHeight / 2)

        val snackbarPlaceables = subcompose(BottomSheetScaffoldLayoutSlot.Snackbar, snackbarHost)
            .map { it.measure(looseConstraints) }
        val snackbarWidth = snackbarPlaceables.maxBy { it.width }.width
        val snackbarHeight = snackbarPlaceables.maxBy { it.height }.height
        val snackbarOffsetX = (layoutWidth - snackbarWidth) / 2
        val snackbarOffsetY = when (sheetState.currentValue) {
            BottomSheetValue.Collapsed -> fabOffsetY - snackbarHeight
            BottomSheetValue.Expanded -> layoutHeight - snackbarHeight
        }
        layout(layoutWidth, layoutHeight) {
            // Placement order is important for elevation
            bodyPlaceables.forEach { it.placeRelative(0, topBarHeight) }
            topBarPlaceables?.forEach { it.placeRelative(0, 0) }
            sheetPlaceables.forEach { it.placeRelative(0, sheetOffsetY) }
            fabPlaceable?.forEach { it.placeRelative(fabOffsetX, fabOffsetY) }
            snackbarPlaceables.forEach { it.placeRelative(snackbarOffsetX, snackbarOffsetY) }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("FunctionName", "SameParameterValue")
private fun ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
    state: AnchoredDraggableState<*>,
    orientation: Orientation
): NestedScrollConnection = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.Drag) {
            state.dispatchRawDelta(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.Drag) {
            state.dispatchRawDelta(available.toFloat()).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.toFloat()
        val currentOffset = state.requireOffset()
        return if (toFling < 0 && currentOffset > state.minOffset) {
            state.settle(velocity = toFling)
            // since we go to the anchor with tween settling, consume all for the best UX
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        state.settle(velocity = available.toFloat())
        return available
    }

    private fun Float.toOffset(): Offset = Offset(
        x = if (orientation == Orientation.Horizontal) this else 0f,
        y = if (orientation == Orientation.Vertical) this else 0f
    )

    @JvmName("velocityToFloat")
    private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

    @JvmName("offsetToFloat")
    private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("FunctionName")
private fun BottomSheetScaffoldAnchorChangeCallback(
    state: BottomSheetState,
    scope: CoroutineScope
) = AnchoredDraggableState.AnchorChangedCallback<BottomSheetValue> { prevTarget, prevAnchors, newAnchors ->
        val previousTargetOffset = prevAnchors[prevTarget]
        val newTarget = when (prevTarget) {
            BottomSheetValue.Collapsed -> BottomSheetValue.Collapsed
            BottomSheetValue.Expanded -> if (newAnchors.containsKey(BottomSheetValue.Expanded)) {
                BottomSheetValue.Expanded
            } else {
                BottomSheetValue.Collapsed
            }
        }
        val newTargetOffset = newAnchors.getValue(newTarget)
        if (newTargetOffset != previousTargetOffset) {
            if (state.isAnimationRunning) {
                // Re-target the animation to the new offset if it changed
                scope.launch { state.animateTo(newTarget, velocity = state.lastVelocity) }
            } else {
                // Snap to the new offset value of the target if no animation was running
                val didSnapSynchronously = state.trySnapTo(newTarget)
                if (!didSnapSynchronously) scope.launch { state.snapTo(newTarget) }
            }
        }
    }

@Suppress("PrivatePropertyName")
private val FabSpacing = 16.dp
@Suppress("PrivatePropertyName")
private val BottomSheetScaffoldPositionalThreshold = 56.dp
@Suppress("PrivatePropertyName")
private val BottomSheetScaffoldVelocityThreshold = 125.dp

/**
 * Enable drag gestures between a set of predefined values.
 *
 * When a drag is detected, the offset of the [AnchoredDraggableState] will be updated with the drag
 * delta. You should use this offset to move your content accordingly (see [Modifier.offset]).
 * When the drag ends, the offset will be animated to one of the anchors and when that anchor is
 * reached, the value of the [AnchoredDraggableState] will also be updated to the value
 * corresponding to the new anchor.
 *
 * Dragging is constrained between the minimum and maximum anchors.
 *
 * @param state The associated [AnchoredDraggableState].
 * @param orientation The orientation in which the [anchoredDraggable] can be dragged.
 * @param enabled Whether this [anchoredDraggable] is enabled and should react to the user's input.
 * @param reverseDirection Whether to reverse the direction of the drag, so a top to bottom
 * drag will behave like bottom to top, and a left to right drag will behave like right to left.
 * @param interactionSource Optional [MutableInteractionSource] that will passed on to
 * the internal [Modifier.draggable].
 */
@ExperimentalMaterialApi
internal fun <T> Modifier.anchoredDraggable(
    state: AnchoredDraggableState<T>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null
) = draggable(
    state = state.draggableState,
    orientation = orientation,
    enabled = enabled,
    interactionSource = interactionSource,
    reverseDirection = reverseDirection,
    startDragImmediately = state.isAnimationRunning,
    onDragStopped = { velocity -> launch { state.settle(velocity) } }
)

/**
 * Scope used for suspending anchored drag blocks. Allows to set [AnchoredDraggableState.offset] to
 * a new value.
 *
 * @see [AnchoredDraggableState.anchoredDrag] to learn how to start the anchored drag and get the
 * access to this scope.
 */
internal interface AnchoredDragScope {
    /**
     * Assign a new value for an offset value for [AnchoredDraggableState].
     *
     * @param newOffset new value for [AnchoredDraggableState.offset].
     * @param lastKnownVelocity last known velocity (if known)
     */
    fun dragTo(
        newOffset: Float,
        lastKnownVelocity: Float = 0f
    )
}

/**
 * State of the [anchoredDraggable] modifier.
 *
 * This contains necessary information about any ongoing drag or animation and provides methods
 * to change the state either immediately or by starting an animation. To create and remember a
 * [AnchoredDraggableState] use [rememberAnchoredDraggableState].
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 * @param positionalThreshold The positional threshold, in px, to be used when calculating the
 * target state while a drag is in progress and when settling after the drag ends. This is the
 * distance from the start of a transition. It will be, depending on the direction of the
 * interaction, added or subtracted from/to the origin offset. It should always be a positive value.
 * @param velocityThreshold The velocity threshold (in px per second) that the end velocity has to
 * exceed in order to animate to the next state, even if the [positionalThreshold] has not been
 * reached.
 */
@Stable
@ExperimentalMaterialApi
internal class AnchoredDraggableState<T>(
    initialValue: T,
    internal val positionalThreshold: (totalDistance: Float) -> Float,
    internal val velocityThreshold: () -> Float,
    val animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    internal val confirmValueChange: (newValue: T) -> Boolean = { true }
) {

    private val dragMutex = InternalMutatorMutex()

    internal val draggableState = object : DraggableState {

        private val dragScope = object : DragScope {
            override fun dragBy(pixels: Float) {
                with(anchoredDragScope) {
                    dragTo(newOffsetForDelta(pixels))
                }
            }
        }

        override suspend fun drag(
            dragPriority: MutatePriority,
            block: suspend DragScope.() -> Unit
        ) {
            this@AnchoredDraggableState.anchoredDrag(dragPriority) {
                with(dragScope) { block() }
            }
        }

        override fun dispatchRawDelta(delta: Float) {
            this@AnchoredDraggableState.dispatchRawDelta(delta)
        }
    }

    /**
     * The current value of the [AnchoredDraggableState].
     */
    var currentValue: T by mutableStateOf(initialValue)
        private set

    /**
     * The target value. This is the closest value to the current offset, taking into account
     * positional thresholds. If no interactions like animations or drags are in progress, this
     * will be the current value.
     */
    val targetValue: T by derivedStateOf {
        animationTarget ?: run {
            val currentOffset = offset
            if (!currentOffset.isNaN()) {
                computeTarget(currentOffset, currentValue, velocity = 0f)
            } else currentValue
        }
    }

    /**
     * The closest value in the swipe direction from the current offset, not considering thresholds.
     * If an [anchoredDrag] is in progress, this will be the target of that anchoredDrag (if
     * specified).
     */
    private val closestValue: T by derivedStateOf {
        animationTarget ?: run {
            val currentOffset = offset
            if (!currentOffset.isNaN()) {
                computeTargetWithoutThresholds(currentOffset, currentValue)
            } else currentValue
        }
    }

    /**
     * The current offset, or [Float.NaN] if it has not been initialized yet.
     *
     * The offset will be initialized when the anchors are first set through [updateAnchors].
     *
     * Strongly consider using [requireOffset] which will throw if the offset is read before it is
     * initialized. This helps catch issues early in your workflow.
     */
    @Suppress("AutoboxingStateCreation")
    var offset: Float by mutableStateOf(Float.NaN)
        private set

    /**
     * Require the current offset.
     *
     * @see offset
     *
     * @throws IllegalStateException If the offset has not been initialized yet
     */
    fun requireOffset(): Float {
        check(!offset.isNaN()) {
            "The offset was read before being initialized. Did you access the offset in a phase " +
                    "before layout, like effects or composition?"
        }
        return offset
    }

    /**
     * Whether an animation is currently in progress.
     */
    val isAnimationRunning: Boolean get() = animationTarget != null

    /**
     * The fraction of the progress going from [currentValue] to [closestValue], within [0f..1f]
     * bounds, or 1f if the [AnchoredDraggableState] is in a settled state.
     */
    /*@FloatRange(from = 0f, to = 1f)*/
    val progress: Float by derivedStateOf(structuralEqualityPolicy()) {
        val a = anchors[currentValue] ?: 0f
        val b = anchors[closestValue] ?: 0f
        val distance = abs(b - a)
        if (distance > 1e-6f) {
            val progress = (this.requireOffset() - a) / (b - a)
            // If we are very close to 0f or 1f, we round to the closest
            if (progress < 1e-6f) 0f else if (progress > 1 - 1e-6f) 1f else progress
        } else 0f
    }

    /**
     * The velocity of the last known animation. Gets reset to 0f when an animation completes
     * successfully, but does not get reset when an animation gets interrupted.
     * You can use this value to provide smooth reconciliation behavior when re-targeting an
     * animation.
     */
    var lastVelocity: Float by mutableFloatStateOf(0f)
        private set

    /**
     * The minimum offset this state can reach. This will be the smallest anchor, or
     * [Float.NEGATIVE_INFINITY] if the anchors are not initialized yet.
     */
    val minOffset by derivedStateOf { anchors.minOrNull() ?: Float.NEGATIVE_INFINITY }

    /**
     * The maximum offset this state can reach. This will be the biggest anchor, or
     * [Float.POSITIVE_INFINITY] if the anchors are not initialized yet.
     */
    private val maxOffset by derivedStateOf { anchors.maxOrNull() ?: Float.POSITIVE_INFINITY }

    private var animationTarget: T? by mutableStateOf(null)

    internal var anchors by mutableStateOf(emptyMap<T, Float>())

    /**
     * Update the anchors.
     * If the previous set of anchors was empty, attempt to update the offset to match the initial
     * value's anchor. If the [newAnchors] are different to the existing anchors, or there is no
     * anchor for the [currentValue], the [onAnchorsChanged] callback will be invoked.
     *
     * <b>If your anchors depend on the size of the layout, updateAnchors should be called in the
     * layout (placement) phase, e.g. through Modifier.onSizeChanged.</b> This ensures that the
     * state is set up within the same frame.
     * For static anchors, or anchors with different data dependencies, updateAnchors is safe to be
     * called any time, for example from a side effect.
     *
     * @param newAnchors The new anchors
     * @param onAnchorsChanged Optional callback to be invoked if the state needs to be updated
     * after updating the anchors, for example if the anchor for the [currentValue] has been removed
     */
    internal fun updateAnchors(
        newAnchors: Map<T, Float>,
        onAnchorsChanged: AnchorChangedCallback<T>? = null
    ) {
        if (anchors != newAnchors) {
            val previousAnchors = anchors
            val previousTarget = targetValue
            val previousAnchorsEmpty = anchors.isEmpty()
            anchors = newAnchors

            val currentValueHasAnchor = anchors[currentValue] != null
            if (previousAnchorsEmpty && currentValueHasAnchor) {
                trySnapTo(currentValue)
            } else {
                onAnchorsChanged?.onAnchorsChanged(
                    previousTargetValue = previousTarget,
                    previousAnchors = previousAnchors,
                    newAnchors = newAnchors
                )
            }
        }
    }

    /**
     * Whether the [value] has an anchor associated with it.
     */
    fun hasAnchorForValue(value: T): Boolean = anchors.containsKey(value)

    /**
     * Find the closest anchor taking into account the velocity and settle at it with an animation.
     */
    suspend fun settle(velocity: Float) {
        val previousValue = this.currentValue
        val targetValue = computeTarget(
            offset = requireOffset(),
            currentValue = previousValue,
            velocity = velocity
        )
        if (confirmValueChange(targetValue)) {
            animateTo(targetValue, velocity)
        } else {
            // If the user vetoed the state change, rollback to the previous state.
            animateTo(previousValue, velocity)
        }
    }

    private fun computeTarget(
        offset: Float,
        currentValue: T,
        velocity: Float
    ): T {
        val currentAnchors = anchors
        val currentAnchor = currentAnchors[currentValue]
        val velocityThresholdPx = velocityThreshold()
        return if (currentAnchor == offset || currentAnchor == null) {
            currentValue
        } else if (currentAnchor < offset) {
            // Swiping from lower to upper (positive).
            if (velocity >= velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, true)
            } else {
                val upper = currentAnchors.closestAnchor(offset, true)
                val distance = abs(currentAnchors.getValue(upper) - currentAnchor)
                val relativeThreshold = abs(positionalThreshold(distance))
                val absoluteThreshold = abs(currentAnchor + relativeThreshold)
                if (offset < absoluteThreshold) currentValue else upper
            }
        } else {
            // Swiping from upper to lower (negative).
            if (velocity <= -velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, false)
            } else {
                val lower = currentAnchors.closestAnchor(offset, false)
                val distance = abs(currentAnchor - currentAnchors.getValue(lower))
                val relativeThreshold = abs(positionalThreshold(distance))
                val absoluteThreshold = abs(currentAnchor - relativeThreshold)
                if (offset < 0) {
                    // For negative offsets, larger absolute thresholds are closer to lower anchors
                    // than smaller ones.
                    if (abs(offset) < absoluteThreshold) currentValue else lower
                } else {
                    if (offset > absoluteThreshold) currentValue else lower
                }
            }
        }
    }

    private fun computeTargetWithoutThresholds(
        offset: Float,
        currentValue: T,
    ): T {
        val currentAnchors = anchors
        val currentAnchor = currentAnchors[currentValue]
        return if (currentAnchor == offset || currentAnchor == null) {
            currentValue
        } else if (currentAnchor < offset) {
            currentAnchors.closestAnchor(offset, true)
        } else {
            currentAnchors.closestAnchor(offset, false)
        }
    }

    private val anchoredDragScope: AnchoredDragScope = object : AnchoredDragScope {
        override fun dragTo(newOffset: Float, lastKnownVelocity: Float) {
            offset = newOffset
            lastVelocity = lastKnownVelocity
        }
    }

    /**
     * Call this function to take control of drag logic and perform anchored drag.
     *
     * All actions that change the [offset] of this [AnchoredDraggableState] must be performed
     * within an [anchoredDrag] block (even if they don't call any other methods on this object)
     * in order to guarantee that mutual exclusion is enforced.
     *
     * If [anchoredDrag] is called from elsewhere with the [dragPriority] higher or equal to ongoing
     * drag, ongoing drag will be canceled.
     *
     * @param dragPriority of the drag operation
     * @param block perform anchored drag given the current anchor provided
     */
    suspend fun anchoredDrag(
        dragPriority: MutatePriority = MutatePriority.Default,
        block: suspend AnchoredDragScope.(anchors: Map<T, Float>) -> Unit
    ): Unit = doAnchoredDrag(null, dragPriority, block)

    /**
     * Call this function to take control of drag logic and perform anchored drag.
     *
     * All actions that change the [offset] of this [AnchoredDraggableState] must be performed
     * within an [anchoredDrag] block (even if they don't call any other methods on this object)
     * in order to guarantee that mutual exclusion is enforced.
     *
     * This overload allows the caller to hint the target value that this [anchoredDrag] is intended
     * to arrive to. This will set [AnchoredDraggableState.targetValue] to provided value so
     * consumers can reflect it in their UIs.
     *
     * If [anchoredDrag] is called from elsewhere with the [dragPriority] higher or equal to ongoing
     * drag, ongoing drag will be canceled.
     *
     * @param targetValue hint the target value that this [anchoredDrag] is intended to arrive to
     * @param dragPriority of the drag operation
     * @param block perform anchored drag given the current anchor provided
     */
    suspend fun anchoredDrag(
        targetValue: T,
        dragPriority: MutatePriority = MutatePriority.Default,
        block: suspend AnchoredDragScope.(anchors: Map<T, Float>) -> Unit
    ): Unit = doAnchoredDrag(targetValue, dragPriority, block)

    private suspend fun doAnchoredDrag(
        targetValue: T?,
        dragPriority: MutatePriority,
        block: suspend AnchoredDragScope.(anchors: Map<T, Float>) -> Unit
    ) = coroutineScope {
        if (targetValue == null || anchors.containsKey(targetValue)) {
            try {
                dragMutex.mutate(dragPriority) {
                    if (targetValue != null) animationTarget = targetValue
                    anchoredDragScope.block(anchors)
                }
            } finally {
                if (targetValue != null) animationTarget = null
                val endState =
                    anchors
                        .entries
                        .firstOrNull { (_, anchorOffset) -> abs(anchorOffset - offset) < 0.5f }
                        ?.key

                if (endState != null && confirmValueChange.invoke(endState)) {
                    currentValue = endState
                }
            }
        } else if (confirmValueChange(targetValue)) {
            currentValue = targetValue
        }
    }

    internal fun newOffsetForDelta(delta: Float) =
        ((if (offset.isNaN()) 0f else offset) + delta).coerceIn(minOffset, maxOffset)

    /**
     * Drag by the [delta], coerce it in the bounds and dispatch it to the [AnchoredDraggableState].
     *
     * @return The delta the consumed by the [AnchoredDraggableState]
     */
    fun dispatchRawDelta(delta: Float): Float {
        val newOffset = newOffsetForDelta(delta)
        val oldOffset = if (offset.isNaN()) 0f else offset
        offset = newOffset
        return newOffset - oldOffset
    }

    /**
     * Attempt to snap synchronously. Snapping can happen synchronously when there is no other drag
     * transaction like a drag or an animation is progress. If there is another interaction in
     * progress, the suspending [snapTo] overload needs to be used.
     *
     * @return true if the synchronous snap was successful, or false if we couldn't snap synchronous
     */
    internal fun trySnapTo(targetValue: T): Boolean = dragMutex.tryMutate {
        with(anchoredDragScope) {
            val targetOffset = anchors[targetValue]
            if (targetOffset != null) {
                dragTo(targetOffset)
                animationTarget = null
            }
            currentValue = targetValue
        }
    }

    companion object {
        /**
         * The default [Saver] implementation for [AnchoredDraggableState].
         */
        @ExperimentalMaterialApi
        fun <T : Any> Saver(
            animationSpec: AnimationSpec<Float>,
            confirmValueChange: (T) -> Boolean,
            positionalThreshold: (distance: Float) -> Float,
            velocityThreshold: () -> Float
        ) = Saver<AnchoredDraggableState<T>, T>(
            save = { it.currentValue },
            restore = {
                AnchoredDraggableState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmValueChange = confirmValueChange,
                    positionalThreshold = positionalThreshold,
                    velocityThreshold = velocityThreshold
                )
            }
        )
    }

    /**
     * Defines a callback that is invoked when the anchors have changed.
     *
     * Components with custom reconciliation logic should implement this callback, for example to
     * re-target an in-progress animation when the anchors change.
     *
     * @see AnchoredDraggableDefaults.ReconcileAnimationOnAnchorChangedCallback for a default
     * implementation
     */
    @ExperimentalMaterialApi
    fun interface AnchorChangedCallback<T> {

        /**
         * Callback that is invoked when the anchors have changed, after the
         * [AnchoredDraggableState] has been updated with them. Use this hook to re-launch
         * animations or interrupt them if needed.
         *
         * @param previousTargetValue The target value before the anchors were updated
         * @param previousAnchors The previously set anchors
         * @param newAnchors The newly set anchors
         */
        fun onAnchorsChanged(
            previousTargetValue: T,
            previousAnchors: Map<T, Float>,
            newAnchors: Map<T, Float>,
        )
    }
}

/**
 * Snap to a [targetValue] without any animation.
 * If the [targetValue] is not in the set of anchors, the [AnchoredDraggableState.currentValue] will
 * be updated to the [targetValue] without updating the offset.
 *
 * @throws CancellationException if the interaction interrupted by another interaction like a
 * gesture interaction or another programmatic interaction like a [animateTo] or [snapTo] call.
 *
 * @param targetValue The target value of the animation
 */
@ExperimentalMaterialApi
internal suspend fun <T> AnchoredDraggableState<T>.snapTo(targetValue: T) {
    anchoredDrag(targetValue = targetValue) { anchors ->
        val targetOffset = anchors[targetValue]
        if (targetOffset != null) dragTo(targetOffset)
    }
}

/**
 * Animate to a [targetValue].
 * If the [targetValue] is not in the set of anchors, the [AnchoredDraggableState.currentValue] will
 * be updated to the [targetValue] without updating the offset.
 *
 * @throws CancellationException if the interaction interrupted by another interaction like a
 * gesture interaction or another programmatic interaction like a [animateTo] or [snapTo] call.
 *
 * @param targetValue The target value of the animation
 * @param velocity The velocity the animation should start with
 */
@ExperimentalMaterialApi
internal suspend fun <T> AnchoredDraggableState<T>.animateTo(
    targetValue: T,
    velocity: Float = this.lastVelocity,
) {
    anchoredDrag(targetValue = targetValue) { anchors ->
        val targetOffset = anchors[targetValue]
        if (targetOffset != null) {
            var prev = if (offset.isNaN()) 0f else offset
            animate(prev, targetOffset, velocity, animationSpec) { value, velocity ->
                // Our onDrag coerces the value within the bounds, but an animation may
                // overshoot, for example a spring animation or an overshooting interpolator
                // We respect the user's intention and allow the overshoot, but still use
                // DraggableState's drag for its mutex.
                dragTo(value, velocity)
                prev = value
            }
        }
    }
}

/**
 * Create and remember a [AnchoredDraggableState].
 *
 * @param initialValue The initial value.
 * @param animationSpec The default animation that will be used to animate to a new value.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending value change.
 */
@Composable
@ExperimentalMaterialApi
internal fun <T : Any> rememberAnchoredDraggableState(
    initialValue: T,
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    confirmValueChange: (newValue: T) -> Boolean = { true }
): AnchoredDraggableState<T> {
    val positionalThreshold = AnchoredDraggableDefaults.positionalThreshold
    val velocityThreshold = AnchoredDraggableDefaults.velocityThreshold
    return rememberSaveable(
        initialValue, animationSpec, confirmValueChange, positionalThreshold, velocityThreshold,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            confirmValueChange = confirmValueChange,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold
        ),
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmValueChange = confirmValueChange,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold
        )
    }
}

/**
 * Contains useful defaults for [anchoredDraggable] and [AnchoredDraggableState].
 */
@Stable
@ExperimentalMaterialApi
internal object AnchoredDraggableDefaults {
    /**
     * The default animation used by [AnchoredDraggableState].
     */
    @get:ExperimentalMaterialApi
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    @ExperimentalMaterialApi
    val AnimationSpec = SpringSpec<Float>()

    /**
     * The default velocity threshold (1.8 dp per millisecond) used by
     * [rememberAnchoredDraggableState].
     */
    @get:ExperimentalMaterialApi
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    @ExperimentalMaterialApi
    val velocityThreshold: () -> Float
        @Composable get() = with(LocalDensity.current) { { 125.dp.toPx() } }

    /**
     * The default positional threshold (56 dp) used by [rememberAnchoredDraggableState]
     */
    @get:ExperimentalMaterialApi
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    @ExperimentalMaterialApi
    val positionalThreshold: (totalDistance: Float) -> Float
        @Composable get() = with(LocalDensity.current) {
            { 56.dp.toPx() }
        }

    @ExperimentalMaterialApi
    @Suppress("FunctionName")
    internal fun <T> ReconcileAnimationOnAnchorChangedCallback(
        state: AnchoredDraggableState<T>,
        scope: CoroutineScope
    ) =
        AnchoredDraggableState.AnchorChangedCallback { previousTarget, previousAnchors, newAnchors ->
            val previousTargetOffset = previousAnchors[previousTarget]
            val newTargetOffset = newAnchors[previousTarget]
            if (previousTargetOffset != newTargetOffset) {
                if (newTargetOffset != null) {
                    scope.launch {
                        state.animateTo(previousTarget, state.lastVelocity)
                    }
                } else {
                    scope.launch {
                        state.snapTo(newAnchors.closestAnchor(offset = state.requireOffset()))
                    }
                }
            }
        }
}

private fun <T> Map<T, Float>.closestAnchor(
    offset: Float = 0f,
    searchUpwards: Boolean = false
): T {
    require(isNotEmpty()) { "The anchors were empty when trying to find the closest anchor" }
    return minBy { (_, anchor) ->
        val delta = if (searchUpwards) anchor - offset else offset - anchor
        if (delta < 0) Float.POSITIVE_INFINITY else delta
    }.key
}

private fun <T> Map<T, Float>.minOrNull() = minOfOrNull { (_, offset) -> offset }
private fun <T> Map<T, Float>.maxOrNull() = maxOfOrNull { (_, offset) -> offset }

@Stable
internal class InternalMutatorMutex {
    private class Mutator(val priority: MutatePriority, val job: Job) {
        fun canInterrupt(other: Mutator) = priority >= other.priority

        fun cancel() = job.cancel()
    }

    private val currentMutator = AtomicReference<Mutator?>(null)
    private val mutex = Mutex()

    private fun tryMutateOrCancel(mutator: Mutator) {
        while (true) {
            val oldMutator = currentMutator.get()
            if (oldMutator == null || mutator.canInterrupt(oldMutator)) {
                if (currentMutator.compareAndSet(oldMutator, mutator)) {
                    oldMutator?.cancel()
                    break
                }
            } else throw CancellationException("Current mutation had a higher priority")
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutate] is called while another call to [mutate] or [mutateWith] is in progress, their
     * [priority] values are compared. If the new caller has a [priority] equal to or higher than
     * the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * @param priority the priority of this mutation; [MutatePriority.Default] by default.
     * Higher priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     */
    suspend fun <R> mutate(
        priority: MutatePriority = MutatePriority.Default,
        block: suspend () -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutateWith] is called while another call to [mutate] or [mutateWith] is in progress,
     * their [priority] values are compared. If the new caller has a [priority] equal to or
     * higher than the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * This variant of [mutate] calls its [block] with a [receiver], removing the need to create
     * an additional capturing lambda to invoke it with a receiver object. This can be used to
     * expose a mutable scope to the provided [block] while leaving the rest of the state object
     * read-only. For example:
     *
     * @param receiver the receiver `this` that [block] will be called with
     * @param priority the priority of this mutation; [MutatePriority.Default] by default.
     * Higher priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     */
    private suspend fun <T, R> mutateWith(
        receiver: T,
        priority: MutatePriority = MutatePriority.Default,
        block: suspend T.() -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                receiver.block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Attempt to mutate synchronously if there is no other active caller.
     * If there is no other active caller, the [block] will be executed in a lock. If there is
     * another active caller, this method will return false, indicating that the active caller
     * needs to be cancelled through a [mutate] or [mutateWith] call with an equal or higher
     * mutation priority.
     *
     * Calls to [mutate] and [mutateWith] will suspend until execution of the [block] has finished.
     *
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     * @return true if the [block] was executed, false if there was another active caller and the
     * [block] was not executed.
     */
    fun tryMutate(block: () -> Unit): Boolean {
        val didLock = mutex.tryLock()
        if (didLock) {
            try {
                block()
            } finally {
                mutex.unlock()
            }
        }
        return didLock
    }
}
