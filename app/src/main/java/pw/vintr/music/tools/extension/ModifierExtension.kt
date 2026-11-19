package pw.vintr.music.tools.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

fun Modifier.scaffoldPadding(scaffoldPadding: PaddingValues) = composed {
    padding(
        top = scaffoldPadding.calculateTopPadding(),
        start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
}

fun Modifier.dialogContainer() = composed {
    navigationBarsPadding()
        .padding(16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(VintrMusicExtendedTheme.colors.dialogBackground)
        .border(
            width = 1.dp,
            color = VintrMusicExtendedTheme.colors.dialogStroke,
            shape = RoundedCornerShape(20.dp)
        )
        .padding(24.dp)
}

fun Modifier.noRippleClickable(onClick: () -> Unit = { }): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}
