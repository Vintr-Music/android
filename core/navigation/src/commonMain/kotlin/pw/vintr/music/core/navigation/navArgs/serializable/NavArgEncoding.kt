package pw.vintr.music.core.navigation.navArgs.serializable

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

val NavJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    classDiscriminator = "type"
}

@OptIn(ExperimentalEncodingApi::class)
private val NavBase64 = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)

@OptIn(ExperimentalEncodingApi::class)
fun String.encodeForNavRoute(): String =
    NavBase64.encode(encodeToByteArray())

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeFromNavRoute(): String =
    NavBase64.decode(this).decodeToString()

fun <T : Any> encodeNavArg(value: T, serializer: KSerializer<T>): String =
    NavJson.encodeToString(serializer, value).encodeForNavRoute()

inline fun <reified T : Any> encodeNavArg(value: T): String =
    encodeNavArg(value, serializer())

fun <T : Any> decodeNavArg(value: String, serializer: KSerializer<T>): T =
    NavJson.decodeFromString(serializer, value.decodeFromNavRoute())

inline fun <reified T : Any> decodeNavArg(value: String): T =
    decodeNavArg(value, serializer())
