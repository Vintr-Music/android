package pw.vintr.music.core.ui.tools

object DurationFormat {

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

        fun pad(value: Int) = value.toString().padStart(2, '0')

        return when {
            days > 0 -> "$days:${pad(hours)}:${pad(minutes)}:${pad(seconds)}"
            hours > 0 -> "$hours:${pad(minutes)}:${pad(seconds)}"
            minutes > 0 -> "$minutes:${pad(seconds)}"
            else -> "0:${pad(seconds)}"
        }
    }
}
