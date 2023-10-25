package pw.vintr.music.ui.kit.toolbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.Gray5

@Composable
fun ToolbarPrimaryMount(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize(),
        onDraw = {
            val rect = Rect(Offset.Zero, size)
            val fillRect = Rect(
                left = Offset.Zero.x,
                top = Offset.Zero.y,
                right = size.width,
                bottom = size.height - 120.dp.toPx()
            )
            val path = Path().apply {
                val xDisplacedCenter = Offset(
                    x = rect.bottomCenter.x + (rect.bottomCenter.x / 2),
                    y = rect.bottomCenter.y
                )
                moveTo(
                    x = rect.topLeft.x,
                    y = rect.topLeft.y
                )
                lineTo(
                    x = rect.bottomLeft.x,
                    y = rect.bottomLeft.y - 80.dp.toPx()
                )
                lineTo(
                    x = xDisplacedCenter.x,
                    y = xDisplacedCenter.y
                )
                lineTo(
                    x = rect.bottomRight.x,
                    y = rect.bottomRight.y - 100.dp.toPx()
                )
                lineTo(
                    x = rect.topRight.x,
                    y = rect.topRight.y
                )
            }

            drawIntoCanvas { canvas ->
                canvas.drawOutline(
                    outline = Outline.Generic(path),
                    paint = Paint().apply {
                        color = Gray5
                        pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                    }
                )
                canvas.drawRect(
                    fillRect,
                    paint = Paint().apply {
                        color = Gray5
                    }
                )
            }
        }
    )
}
