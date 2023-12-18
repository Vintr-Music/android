package pw.vintr.music.data.equalizer.repository

import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import pw.vintr.music.data.equalizer.source.EqualizerCacheDataStore

class EqualizerRepository(
    private val equalizerCacheDataStore: EqualizerCacheDataStore
) {

    suspend fun saveEqualizer(equalizer: EqualizerCacheObject) {
        equalizerCacheDataStore.saveEqualizer(equalizer)
    }

    suspend fun getEqualizer() = equalizerCacheDataStore.getEqualizer()
}
