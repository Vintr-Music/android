package pw.vintr.music.domain.settings

import pw.vintr.music.data.settings.repository.SettingsRepository
import pw.vintr.music.domain.base.BaseInteractor

class PlaybackSettingsInteractor(
    private val repository: SettingsRepository,
) : BaseInteractor() {

    fun setNeedSpeakerNotification(value: Boolean) {
        repository.setNeedSpeakerNotification(value)
    }

    fun getNeedSpeakerNotification() = repository.getNeedSpeakerNotification()

    fun getNeedSpeakerNotificationFlow() = repository.getNeedSpeakerNotificationFlow()
}
