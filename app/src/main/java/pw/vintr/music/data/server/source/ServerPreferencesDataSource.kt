package pw.vintr.music.data.server.source

import android.content.SharedPreferences
import androidx.core.content.edit

class ServerPreferencesDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val KEY_SERVER_ID = "server-id"
    }

    fun setSelectedServerId(serverId: String) {
        sharedPreferences.edit { putString(KEY_SERVER_ID, serverId) }
    }

    fun getSelectedServerId() = sharedPreferences.getString(KEY_SERVER_ID, null)
}
