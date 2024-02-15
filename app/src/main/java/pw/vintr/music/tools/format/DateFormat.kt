package pw.vintr.music.tools.format

import pw.vintr.music.tools.extension.safeFormat
import pw.vintr.music.tools.extension.safeParse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {

    private const val INPUT_DATE_PATTERN = "dd-MM-yyyy"
    private const val OUTPUT_DATE_PATTERN = "dd.MM.yyyy"

    fun parseDate(input: String?): Date? = SimpleDateFormat(
        INPUT_DATE_PATTERN,
        Locale.getDefault()
    ).safeParse(input)

    @JvmName("formatNonNullDate")
    fun formatDate(input: Date): String = SimpleDateFormat(
        OUTPUT_DATE_PATTERN,
        Locale.getDefault()
    ).format(input)

    @JvmName("formatNullableDate")
    fun formatDate(input: Date?): String? = SimpleDateFormat(
        OUTPUT_DATE_PATTERN,
        Locale.getDefault()
    ).safeFormat(input)
}
