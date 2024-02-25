package pw.vintr.music.data.settings.source

import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsPreferenceDataSource(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_SPEAKER_NOTIFICATION = "need-speaker-notification"
    }

    fun setNeedSpeakerNotification(value: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_SPEAKER_NOTIFICATION, value) }
    }

    fun getNeedSpeakerNotification() = sharedPreferences
        .getBoolean(KEY_SPEAKER_NOTIFICATION, false)
}
