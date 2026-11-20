package pw.vintr.music.tools.extension

import io.ktor.utils.io.charsets.Charset
import java.net.URLEncoder

fun String.urlEncode(charset: Charset = Charsets.UTF_8) = URLEncoder
    .encode(this, charset.name())
    .replace("+", "%20")
    .replace("%28", "(")
    .replace("%29", ")")
