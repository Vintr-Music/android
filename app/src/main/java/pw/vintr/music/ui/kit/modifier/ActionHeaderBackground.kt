package pw.vintr.music.ui.kit.modifier

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush

fun Modifier.actionHeaderBackground() = composed {
    background(
        brush = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background
                    .copy(alpha = 1.0f),
                MaterialTheme.colorScheme.background
                    .copy(alpha = 0.9f),
                MaterialTheme.colorScheme.background
                    .copy(alpha = 0.7f),
                MaterialTheme.colorScheme.background
                    .copy(alpha = 0.4f),
                MaterialTheme.colorScheme.background
                    .copy(alpha = 0f),
            )
        )
    )
}
