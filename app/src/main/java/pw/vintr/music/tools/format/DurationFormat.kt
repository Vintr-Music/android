package pw.vintr.music.tools.format

object DurationFormat {

    fun formatSeconds(seconds: Long) = format(millis = seconds * 1000)

    fun format(millis: Long): String {
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
                "$days:$hours:$minutes:$seconds"
            }
            hours > 0 -> {
                "$hours:$minutes:$seconds"
            }
            minutes > 0 -> {
                "$minutes:$seconds"
            }
            else -> {
                "0:$seconds"
            }
        }
    }
}
