package pw.vintr.music.ui.kit.visualizer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.theme.Gradient0
import pw.vintr.music.ui.theme.Gradient1
import kotlin.math.log10

private const val MIN_BARS_COUNT = 10
private const val MAX_BARS_COUNT = 50

private const val DEFAULT_MIN_BAR_HEIGHT = 3F

private val BarWidth = 3.dp
private val GapWidth = 2.dp

private val UnitWidth = 8.dp

@Composable
fun Visualizer(
    modifier: Modifier = Modifier,
    bytes: List<Byte> = listOf(),
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val maxWidthDp = with(LocalDensity.current) { constraints.maxWidth.toDp() }

        val barsCount = remember(constraints.maxWidth) {
            val count = (maxWidthDp / UnitWidth)
                .toInt()
                .coerceIn(MIN_BARS_COUNT, MAX_BARS_COUNT)

            count
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center,
        ) {
            val div = bytes.size / barsCount

            for (i in 0 until barsCount) {
                fun calculateDbValue(): Float {
                    val bytePosition = i * div
                    val rfk = bytes[bytePosition]
                    val ifk = bytes[i + 1]
                    val magnitude = (rfk * rfk + ifk * ifk).toFloat()

                    return (25 * log10(magnitude.toDouble()).toFloat())
                        .coerceAtLeast(DEFAULT_MIN_BAR_HEIGHT)
                }

                val dbValue = if (bytes.isNotEmpty()) {
                    calculateDbValue()
                } else {
                    DEFAULT_MIN_BAR_HEIGHT
                }

                VisualizerBar(height = dbValue.dp)
            }
        }
    }
}

@Composable
private fun VisualizerBar(
    height: Dp,
    brush: Brush = Brush.verticalGradient(listOf(Gradient1, Gradient0))
) {
    val animatedHeight = animateDpAsState(targetValue = height, label = String.Empty)

    Box(
        modifier = Modifier
            .padding(GapWidth)
            .background(brush)
            .height(animatedHeight.value)
            .width(BarWidth)
    )
}
