package pw.vintr.music.ui.feature.menu

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class MenuViewModel : BaseViewModel() {

    fun openSettings() {
        navigator.forward(Screen.Settings)
    }
}
