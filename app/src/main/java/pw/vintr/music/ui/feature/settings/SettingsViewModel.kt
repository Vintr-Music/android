package pw.vintr.music.ui.feature.settings

import kotlinx.coroutines.flow.map
import pw.vintr.music.domain.settings.PlaybackSettingsInteractor
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class SettingsViewModel(
    private val playbackSettingsInteractor: PlaybackSettingsInteractor,
) : BaseViewModel() {

    val screenState = playbackSettingsInteractor
        .getNeedSpeakerNotificationFlow()
        .map { SettingsState(needSpeakerNotification = it) }
        .stateInThis(
            initialValue = SettingsState(
                needSpeakerNotification = playbackSettingsInteractor
                    .getNeedSpeakerNotification()
            )
        )

    fun openEqualizer() {
        navigator.forward(Screen.Equalizer)
    }

    fun setNeedSpeakerNotification(value: Boolean) {
        playbackSettingsInteractor.setNeedSpeakerNotification(value)
    }
}

data class SettingsState(
    val needSpeakerNotification: Boolean = false,
)
