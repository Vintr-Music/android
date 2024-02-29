package pw.vintr.music.data.equalizer.source

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject

class EqualizerPreferencesDataSource(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) {
    companion object {
        private const val KEY_EQUALIZER = "equalizer"
    }

    fun setEqualizer(equalizerCacheObject: EqualizerCacheObject) {
        sharedPreferences.edit { putString(KEY_EQUALIZER, gson.toJson(equalizerCacheObject)) }
    }

    fun getEqualizer() = sharedPreferences.getString(KEY_EQUALIZER, null)?.let {
        gson.fromJson(it, EqualizerCacheObject::class.java)
    }

    fun removeEqualizer() = sharedPreferences.edit { remove(KEY_EQUALIZER) }
}
