package pw.vintr.music.tools.format

import java.text.DecimalFormat

object DurationFormat {

    private val zeroSuffixFormat = DecimalFormat("00")

    fun formatSeconds(seconds: Long) = formatMillis(millis = seconds * 1000)

    fun formatMillis(millis: Long): String {
        val secMilSec: Long = 1000
        val minMilSec = 60 * secMilSec
        val hourMilSec = 60 * minMilSec
        val dayMilSec = 24 * hourMilSec

        val days = (millis / dayMilSec).toInt()
        val hours = (millis % dayMilSec / hourMilSec).toInt()
        val minutes = (millis % dayMilSec % hourMilSec / minMilSec).toInt()
        val seconds = (millis % dayMilSec % hourMilSec % minMilSec / secMilSec).toInt()

        return when {
            days > 0 -> {
                val formatHours = zeroSuffixFormat.format(hours)
                val formatMinutes = zeroSuffixFormat.format(minutes)
                val formatSeconds = zeroSuffixFormat.format(minutes)

                "$days:$formatHours:$formatMinutes:$formatSeconds"
            }
            hours > 0 -> {
                val formatMinutes = zeroSuffixFormat.format(minutes)
                val formatSeconds = zeroSuffixFormat.format(minutes)

                "$hours:$formatMinutes:$formatSeconds"
            }
            minutes > 0 -> {
                "$minutes:${zeroSuffixFormat.format(seconds)}"
            }
            else -> {
                "0:${zeroSuffixFormat.format(seconds)}"
            }
        }
    }
}
