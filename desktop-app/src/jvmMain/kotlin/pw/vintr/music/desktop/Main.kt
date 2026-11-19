package pw.vintr.music.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import pw.vintr.music.core.ui.theme.VintrMusicTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Vintr Music",
    ) {
        VintrMusicTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Vintr Music")
            }
        }
    }
}
