//noinspection UsingMaterialAndMaterial3Libraries
package pw.vintr.music.core.navigation.bottomSheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import androidx.navigation.compose.LocalOwnersProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.transform

@Stable
class BottomSheetNavigatorSheetState(private val sheetState: ModalBottomSheetState) {
    val isVisible: Boolean
        get() = sheetState.isVisible

    val currentValue: ModalBottomSheetValue
        get() = sheetState.currentValue

    val targetValue: ModalBottomSheetValue
        get() = sheetState.targetValue
}

/**
 * Navigator that drives a [ModalBottomSheetState] for use of [ModalBottomSheetLayout]s
 * with the navigation library.
 *
 * Vendored/adapted for Compose Multiplatform — platform instances are created via
 * [createBottomSheetNavigator].
 */
abstract class BottomSheetNavigator(
    val sheetState: ModalBottomSheetState
) : Navigator<BottomSheetNavigator.Destination>() {

    companion object {
        const val NAME = "BottomSheetNavigator"
    }

    private var attached by mutableStateOf(false)

    private val backStack: StateFlow<List<NavBackStackEntry>>
        get() = if (attached) {
            state.backStack
        } else {
            MutableStateFlow(emptyList())
        }

    private val transitionsInProgress: StateFlow<Set<NavBackStackEntry>>
        get() = if (attached) {
            state.transitionsInProgress
        } else {
            MutableStateFlow(emptySet())
        }

    val navigatorSheetState: BottomSheetNavigatorSheetState =
        BottomSheetNavigatorSheetState(sheetState)

    val sheetContent: @Composable ColumnScope.() -> Unit = {
        val saveableStateHolder = rememberSaveableStateHolder()
        val transitionsInProgressEntries by transitionsInProgress.collectAsState()

        val retainedEntry by produceState<NavBackStackEntry?>(
            initialValue = null,
            key1 = backStack
        ) {
            backStack
                .transform { backStackEntries ->
                    try {
                        sheetState.hide()
                    } catch (_: CancellationException) {
                        // Ignored — do not cancel produceState
                    } finally {
                        emit(backStackEntries.lastOrNull())
                    }
                }
                .collect {
                    value = it
                }
        }

        if (retainedEntry != null) {
            LaunchedEffect(retainedEntry) {
                sheetState.show()
            }
        }

        SheetContentHost(
            backStackEntry = retainedEntry,
            sheetState = sheetState,
            saveableStateHolder = saveableStateHolder,
            onSheetShown = {
                transitionsInProgressEntries.forEach(state::markTransitionComplete)
            },
            onSheetDismissed = { backStackEntry ->
                if (transitionsInProgressEntries.contains(backStackEntry)) {
                    state.markTransitionComplete(backStackEntry)
                } else {
                    state.pop(popUpTo = backStackEntry, saveState = false)
                }
            }
        )
    }

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        attached = true
    }

    override fun createDestination(): Destination = Destination(
        navigator = this,
        content = {}
    )

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        entries.forEach { entry ->
            state.pushWithTransition(entry)
        }
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
    }

    class Destination(
        navigator: BottomSheetNavigator,
        internal val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
    ) : NavDestination(navigator), FloatingWindow
}

@Composable
fun ColumnScope.SheetContentHost(
    backStackEntry: NavBackStackEntry?,
    sheetState: ModalBottomSheetState,
    saveableStateHolder: SaveableStateHolder,
    onSheetShown: (entry: NavBackStackEntry) -> Unit,
    onSheetDismissed: (entry: NavBackStackEntry) -> Unit,
) {
    if (backStackEntry != null) {
        val currentOnSheetShown by rememberUpdatedState(onSheetShown)
        val currentOnSheetDismissed by rememberUpdatedState(onSheetDismissed)
        LaunchedEffect(sheetState, backStackEntry) {
            snapshotFlow { sheetState.isVisible }
                .distinctUntilChanged()
                .drop(1)
                .collect { visible ->
                    if (visible) {
                        currentOnSheetShown(backStackEntry)
                    } else {
                        currentOnSheetDismissed(backStackEntry)
                    }
                }
        }
        backStackEntry.LocalOwnersProvider(saveableStateHolder) {
            val content =
                (backStackEntry.destination as BottomSheetNavigator.Destination).content
            content(backStackEntry)
        }
    }
}

@Composable
fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = bottomSheetNavigator.sheetState,
        sheetContent = bottomSheetNavigator.sheetContent,
        modifier = modifier,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        sheetContentColor = sheetContentColor,
        scrimColor = scrimColor,
        content = content
    )
}

@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SpringSpec(),
    skipHalfExpanded: Boolean = false,
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec,
        skipHalfExpanded = skipHalfExpanded
    )
    return remember(sheetState) {
        createBottomSheetNavigator(sheetState = sheetState)
    }
}

expect fun createBottomSheetNavigator(
    sheetState: ModalBottomSheetState
): BottomSheetNavigator
