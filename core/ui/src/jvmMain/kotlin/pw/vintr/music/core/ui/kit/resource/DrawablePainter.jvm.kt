package pw.vintr.music.core.ui.kit.resource

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter

@Composable
actual fun drawablePainter(id: Int): Painter = ColorPainter(Color.Gray)
