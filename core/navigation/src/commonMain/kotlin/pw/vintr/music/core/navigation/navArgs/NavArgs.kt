package pw.vintr.music.core.navigation.navArgs

import androidx.savedstate.SavedState
import androidx.savedstate.read
import pw.vintr.music.core.navigation.navArgs.serializable.decodeNavArg

inline fun <reified T : Any> SavedState?.getRequiredArg(key: String): T {
    val raw = requireNotNull(this) { "arguments bundle is null" }
        .read { getStringOrNull(key) }
        ?: error("argument for $key is null")
    return decodeNavArg(raw)
}

inline fun <reified T : Any> SavedState?.getRequiredArgList(key: String): List<T> {
    return getRequiredArg(key)
}

fun SavedState?.getStringArg(key: String, default: String = ""): String {
    return this?.read { getStringOrNull(key) } ?: default
}

fun SavedState?.getBooleanArg(key: String, default: Boolean = false): Boolean {
    if (this == null) return default
    return read {
        runCatching { getBoolean(key) }.getOrNull()
            ?: getStringOrNull(key)?.toBooleanStrictOrNull()
            ?: default
    }
}
