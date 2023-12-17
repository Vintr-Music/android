package pw.vintr.music.ui.feature.settings

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class SettingsViewModel : BaseViewModel() {

    fun openEqualizer() {
        navigator.forward(Screen.Equalizer)
    }
}
