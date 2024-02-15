package pw.vintr.music.ui.kit.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderPositions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import pw.vintr.music.tools.format.DurationFormat
import pw.vintr.music.ui.theme.Gilroy12
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    trackDuration: Float = 0f,
    onSeek: (Float) -> Unit = {},
    onSeekEnd: () -> Unit = {},
) {
    Box(modifier = modifier) {
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = progress,
            onValueChange = { onSeek(it) },
            onValueChangeFinished = { onSeekEnd() },
            valueRange = 0f..trackDuration,
            track = { sliderPositions ->
                SliderTrack(sliderPositions)
            },
            thumb = {
                Spacer(Modifier.size(32.dp))
            }
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp),
            text = DurationFormat.formatMillis(progress.toLong()),
            style = Gilroy12,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp),
            text = DurationFormat.formatMillis(trackDuration.toLong()),
            style = Gilroy12,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
    }
}

@Composable
private fun SliderTrack(sliderPositions: SliderPositions) {
    val inactiveTrackColor = VintrMusicExtendedTheme.colors
        .playerSliderInactiveBackground
        .copy(alpha = 0.5f)
    val activeTrackColor = VintrMusicExtendedTheme.colors
        .playerSliderActiveBackground
        .copy(alpha = 0.8f)
    val strokeColor = VintrMusicExtendedTheme.colors.playerSliderStroke

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, strokeColor, RoundedCornerShape(10.dp)),
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight

        val trackStrokeWidth = 32.dp.toPx()
        val markerWidth = 1.dp.toPx()

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

        val markerValueStart = Offset(
            x = activeSliderValueEnd.x - markerWidth,
            y = center.y
        )

        // Active track
        drawLine(
            activeTrackColor,
            activeSliderValueStart,
            activeSliderValueEnd,
            trackStrokeWidth,
        )
        // Active marker
        drawLine(
            strokeColor,
            markerValueStart,
            activeSliderValueEnd,
            trackStrokeWidth,
        )
    }
}
