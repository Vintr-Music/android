package pw.vintr.music.tools.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalLayoutDirection

fun Modifier.scaffoldPadding(scaffoldPadding: PaddingValues) = composed {
    padding(
        top = scaffoldPadding.calculateTopPadding(),
        start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
}

fun Modifier.noRippleClickable(onClick: () -> Unit = { }): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}
