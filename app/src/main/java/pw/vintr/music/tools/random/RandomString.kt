package pw.vintr.music.tools.random

import pw.vintr.music.tools.extension.Empty
import kotlin.random.Random

fun generateRandomString(length: Int): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..length)
        .map { Random.nextInt(from = 0, until = charPool.size) }
        .map(charPool::get)
        .joinToString(String.Empty)
}
