package pw.vintr.music.app.main

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { noLocalActivityProvidedFor() }

private fun noLocalActivityProvidedFor(): Nothing {
    error("CompositionLocal LocalActivity not present")
}
