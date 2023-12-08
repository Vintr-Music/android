package pw.vintr.music.ui.kit.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

fun Modifier.cardContainer(shape: Shape = RoundedCornerShape(20.dp)) = composed {
    val backgroundColor = VintrMusicExtendedTheme.colors.cardBackground
    val strokeColor = VintrMusicExtendedTheme.colors.cardStroke

    fillMaxWidth()
        .clip(shape)
        .background(backgroundColor)
        .border(1.dp, SolidColor(strokeColor), shape)
        .padding(20.dp)
}
