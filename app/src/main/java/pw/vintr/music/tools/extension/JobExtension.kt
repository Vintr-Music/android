package pw.vintr.music.tools.extension

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this != null && isActive) {
        cancel()
    }
}
