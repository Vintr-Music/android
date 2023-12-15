package pw.vintr.music.tools.extension

import io.ktor.client.request.HttpRequestBuilder

fun HttpRequestBuilder.encodedParameter(key: String, value: Any?): Unit =
    value?.let { url.encodedParameters.append(key, it.toString()) } ?: Unit
