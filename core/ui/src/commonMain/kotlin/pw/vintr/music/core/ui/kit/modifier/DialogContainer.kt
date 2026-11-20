package pw.vintr.music.core.ui.kit.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

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
