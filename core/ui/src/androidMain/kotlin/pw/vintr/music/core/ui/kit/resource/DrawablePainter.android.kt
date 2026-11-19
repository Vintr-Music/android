package pw.vintr.music.core.ui.kit.resource

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
actual fun drawablePainter(id: Int): Painter = painterResource(id = id)
