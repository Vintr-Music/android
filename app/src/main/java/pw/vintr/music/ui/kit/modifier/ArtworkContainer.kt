package pw.vintr.music.ui.kit.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

fun Modifier.artworkContainer(shape: Shape) = composed {
    val backgroundColor = VintrMusicExtendedTheme.colors.coverBackground

    fillMaxWidth()
        .aspectRatio(ratio = 1f)
        .clip(shape)
        .background(backgroundColor)
        .border(1.dp, SolidColor(backgroundColor), shape)
}
