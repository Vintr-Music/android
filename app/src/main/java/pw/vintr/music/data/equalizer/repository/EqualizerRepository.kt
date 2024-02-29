package pw.vintr.music.data.equalizer.repository

import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import pw.vintr.music.data.equalizer.source.EqualizerCacheDataSource
import pw.vintr.music.data.equalizer.source.EqualizerPreferencesDataSource

class EqualizerRepository(
    private val equalizerCacheDataStore: EqualizerCacheDataSource,
    private val equalizerPreferencesDataSource: EqualizerPreferencesDataSource,
) {

    suspend fun saveEqualizer(equalizer: EqualizerCacheObject) {
        equalizerCacheDataStore.saveEqualizer(equalizer)
        equalizerPreferencesDataSource.setEqualizer(equalizer)
    }

    suspend fun getEqualizer(): EqualizerCacheObject? {
        return equalizerCacheDataStore.getEqualizer() ?: runCatching {
            equalizerPreferencesDataSource.getEqualizer()?.also { restoredData ->
                equalizerCacheDataStore.saveEqualizer(restoredData)
            }
        }.onFailure {
            it.printStackTrace()
            equalizerPreferencesDataSource.removeEqualizer()
        }.getOrNull()
    }
}
