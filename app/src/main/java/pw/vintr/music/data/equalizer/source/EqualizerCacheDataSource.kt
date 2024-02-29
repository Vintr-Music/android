package pw.vintr.music.data.equalizer.source

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EqualizerCacheDataSource(private val realm: Realm) {

    suspend fun saveEqualizer(equalizer: EqualizerCacheObject) {
        realm.write {
            // Find possibly existing equalizer
            val currentEqualizer = query<EqualizerCacheObject>()
                .first()
                .find()

            currentEqualizer?.let {
                // Update existing equalizer
                it.bands = equalizer.bands
                it.presets = equalizer.presets
                it.currentPreset = equalizer.currentPreset
                it.enabled = equalizer.enabled
            } ?: run {
                // Save new equalizer
                copyToRealm(equalizer)
            }
        }
    }

    suspend fun getEqualizer(): EqualizerCacheObject? {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val equalizer = realm.query<EqualizerCacheObject>()
                    .first()
                    .find()
                    ?.copyFromRealm()

                continuation.resume(equalizer)
            }
        }
    }
}
