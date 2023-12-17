package pw.vintr.music.data.audioSession.source

import android.content.SharedPreferences
import androidx.core.content.edit

class AudioSessionPreferencesDataSource(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_SESSION_ID = "session-id"
    }

    fun setSessionId(sessionId: Int) {
        sharedPreferences.edit { putInt(KEY_SESSION_ID, sessionId) }
    }

    fun getSessionId() = sharedPreferences.getInt(KEY_SESSION_ID, -1)
}
