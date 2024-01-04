package pw.vintr.music.data.user.source

import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val KEY_USER_ACCESS_TOKEN = "access-token"
    }

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit { putString(KEY_USER_ACCESS_TOKEN, accessToken) }
    }

    fun getAccessToken() = sharedPreferences.getString(KEY_USER_ACCESS_TOKEN, null)

    fun removeAccessToken() = sharedPreferences.edit { remove(KEY_USER_ACCESS_TOKEN) }
}
