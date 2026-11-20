package pw.vintr.music.core.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

@Composable
internal actual fun ApplyDialogWindowDim() {
    (LocalView.current.parent as? DialogWindowProvider)?.window?.apply {
        setDimAmount(0.9f)
    }
}
