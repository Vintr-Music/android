package pw.vintr.music.core.ui.kit.resource

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

/**
 * Resolves an Android-style drawable resource id to a [Painter].
 * On non-Android targets returns a placeholder until Compose Resources migration.
 */
@Composable
expect fun drawablePainter(id: Int): Painter
