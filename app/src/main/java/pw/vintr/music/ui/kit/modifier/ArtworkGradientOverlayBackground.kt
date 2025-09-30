package pw.vintr.music.ui.kit.modifier

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush

fun Modifier.artworkGradientOverlayBackground(
    top: Float = 0.2f,
) = composed {
    background(
        brush = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background
                    .copy(alpha = top),
                MaterialTheme.colorScheme.background
                    .copy(alpha = 1.0f),
            )
        )
    )
}
