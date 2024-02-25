package pw.vintr.music.data.settings.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.data.settings.source.SettingsPreferenceDataSource

class SettingsRepository(
    private val preferencesDataSource: SettingsPreferenceDataSource
) {

    private val needSpeakerNotificationFlow = MutableStateFlow(
        preferencesDataSource.getNeedSpeakerNotification()
    )

    fun setNeedSpeakerNotification(value: Boolean) {
        preferencesDataSource.setNeedSpeakerNotification(value)
        needSpeakerNotificationFlow.value = value
    }

    fun getNeedSpeakerNotification() = needSpeakerNotificationFlow.value

    fun getNeedSpeakerNotificationFlow() = needSpeakerNotificationFlow.asStateFlow()
}
