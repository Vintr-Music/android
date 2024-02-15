package pw.vintr.music.tools.extension

import java.text.SimpleDateFormat
import java.util.Date

fun SimpleDateFormat.safeParse(date: String?) = try {
    date?.let { this.parse(it) }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun SimpleDateFormat.safeFormat(date: Date?) = try {
    date?.let { this.format(it) }
} catch (e: Exception) {
    null
}
