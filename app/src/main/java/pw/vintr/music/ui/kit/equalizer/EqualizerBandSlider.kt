package pw.vintr.music.ui.kit.equalizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderPositions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.tools.extension.escapePadding
import pw.vintr.music.ui.theme.Gilroy10
import pw.vintr.music.ui.theme.Gradient0
import pw.vintr.music.ui.theme.Gradient1
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun EqualizerBandSlider(
    modifier: Modifier = Modifier,
    currentLevel: Float = 500f,
    lowerLevel: Float = 0f,
    upperLevel: Float = 1000f,
    centerFrequency: Int = 60,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                modifier = Modifier
                    .height(200.dp)
                    .escapePadding(vertical = 16.dp)
                    .graphicsLayer {
                        rotationZ = 270f
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            Constraints(
                                minWidth = constraints.minHeight,
                                maxWidth = constraints.maxHeight,
                                minHeight = constraints.minWidth,
                                maxHeight = constraints.maxWidth,
                            )
                        )
                        layout(placeable.height, placeable.width) {
                            placeable.place(-placeable.width, 0)
                        }
                    },
                value = currentLevel,
                onValueChange = { onValueChange(it) },
                onValueChangeFinished = { onValueChangeFinished() },
                valueRange = lowerLevel..upperLevel,
                track = { sliderPositions ->
                    SliderTrack(sliderPositions)
                },
                thumb = {
                    Spacer(Modifier.size(32.dp))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildString {
                    if (centerFrequency >= 1000000) {
                        append(centerFrequency / 1000000)
                        append(stringResource(id = R.string.khz))
                    } else {
                        append(centerFrequency / 1000)
                        append(stringResource(id = R.string.hz))
                    }
                },
                style = Gilroy10,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
        }
    }
}

@Composable
private fun SliderTrack(sliderPositions: SliderPositions) {
    val inactiveTrackColor = VintrMusicExtendedTheme.colors
        .equalizerSliderBackground
        .copy(alpha = 0.5f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(4.dp)),
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight

        val trackStrokeWidth = 36.dp.toPx()

        drawLine(
            inactiveTrackColor,
            sliderStart,
            sliderEnd,
            trackStrokeWidth,
        )

        val activeSliderValueStart = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.start,
            y = center.y
        )

        val activeSliderValueEnd = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.endInclusive,
            y = center.y
        )

        val activeBrush = Brush.horizontalGradient(listOf(Gradient0, Gradient1))

        // Active track
        drawLine(
            activeBrush,
            activeSliderValueStart,
            activeSliderValueEnd,
            trackStrokeWidth,
        )
    }
}
