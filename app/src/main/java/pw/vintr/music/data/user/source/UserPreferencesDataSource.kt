package pw.vintr.music.data.user.source

import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val KEY_USER_ACCESS_TOKEN = "access-token"
        private const val KEY_USER_ID = "user-id"
    }

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit { putString(KEY_USER_ACCESS_TOKEN, accessToken) }
    }

    fun getAccessToken() = sharedPreferences.getString(KEY_USER_ACCESS_TOKEN, null)

    fun removeAccessToken() = sharedPreferences.edit { remove(KEY_USER_ACCESS_TOKEN) }

    fun setUserId(userId: String) {
        sharedPreferences.edit { putString(KEY_USER_ID, userId) }
    }

    fun getUserId() = sharedPreferences.getString(KEY_USER_ID, null)

    fun removeUserId() = sharedPreferences.edit { remove(KEY_USER_ID) }
}
