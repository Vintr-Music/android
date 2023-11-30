package pw.vintr.music.data.player.source

import android.content.SharedPreferences
import androidx.core.content.edit

class PlayerPreferencesDataStore(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_REPEAT_MODE = "repeat-mode"
        private const val KEY_SHUFFLE_MODE = "shuffle-mode"
    }

    fun setRepeatMode(repeatMode: Int) {
        sharedPreferences.edit { putInt(KEY_REPEAT_MODE, repeatMode) }
    }

    fun getRepeatMode() = sharedPreferences.getInt(KEY_REPEAT_MODE, 0)

    fun setShuffleMode(shuffleMode: Int) {
        sharedPreferences.edit { putInt(KEY_SHUFFLE_MODE, shuffleMode) }
    }

    fun getShuffleMode() = sharedPreferences.getInt(KEY_REPEAT_MODE, 0)
}
