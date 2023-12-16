package pw.vintr.music.tools.extension

import io.ktor.utils.io.charsets.Charset
import java.net.URLEncoder

val String.Companion.Empty: String
    get() = ""

val String.Companion.Space: String
    get() = " "

val String.Companion.Comma: String
    get() = ","

val String.Companion.Dash: String
    get() = "–"

fun String.urlEncode(charset: Charset = Charsets.UTF_8) = URLEncoder
    .encode(this, charset.name())
    .replace("+", "%20")
    .replace("%28", "(")
    .replace("%29", ")")
