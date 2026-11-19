package pw.vintr.music.core.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font

actual val Rubik: FontFamily = FontFamily(
    Font(
        identity = "rubik_regular",
        data = loadFontResource("font/rubik_regular.ttf"),
    ),
    Font(
        identity = "rubik_medium",
        data = loadFontResource("font/rubik_medium.ttf"),
        weight = androidx.compose.ui.text.font.FontWeight.Medium,
    ),
    Font(
        identity = "rubik_bold",
        data = loadFontResource("font/rubik_bold.ttf"),
        weight = androidx.compose.ui.text.font.FontWeight.Bold,
    ),
)

actual val Gilroy: FontFamily = FontFamily(
    Font(
        identity = "gilroy_extra_bold",
        data = loadFontResource("font/gilroy_extra_bold.ttf"),
    ),
)

private fun loadFontResource(path: String): ByteArray {
    val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        ?: error("Font resource not found: $path")
    return stream.use { it.readBytes() }
}
