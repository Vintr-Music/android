package pw.vintr.music.core.ui.tools.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val density = LocalDensity.current
        val imeIsVisible = WindowInsets.ime.getBottom(density) > 0
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

fun Modifier.escapePadding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) = composed {
    val density = LocalDensity.current
    val horizontalPx = with(density) { horizontal.roundToPx() }
    val verticalPx = with(density) { vertical.roundToPx() }

    layout { measurable, constraints ->
        val looseConstraints = constraints.offset(
            horizontal = horizontalPx * 2,
            vertical = verticalPx * 2
        )
        val placeable = measurable.measure(looseConstraints)

        layout(placeable.width, placeable.height) { placeable.placeRelative(x = 0, y = 0) }
    }
}

fun Modifier.scaffoldPadding(scaffoldPadding: PaddingValues) = composed {
    padding(
        top = scaffoldPadding.calculateTopPadding(),
        start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
}

fun Modifier.noRippleClickable(onClick: () -> Unit = { }): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
