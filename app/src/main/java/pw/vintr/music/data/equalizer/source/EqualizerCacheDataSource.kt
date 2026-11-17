package pw.vintr.music.data.equalizer.source

import androidx.room.withTransaction
import pw.vintr.music.data.cache.db.CacheDatabase
import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import pw.vintr.music.data.equalizer.cache.EqualizerDao
import pw.vintr.music.data.equalizer.cache.EqualizerPresetEntity

/**
 * Public API intentionally unchanged vs the Realm implementation:
 * - suspend saveEqualizer(EqualizerCacheObject)
 * - suspend getEqualizer(): EqualizerCacheObject?
 */
class EqualizerCacheDataSource(
    private val database: CacheDatabase,
    private val equalizerDao: EqualizerDao,
) {

    suspend fun saveEqualizer(equalizer: EqualizerCacheObject) {
        database.withTransaction {
            val equalizerRow = equalizer.copy(
                id = EqualizerCacheObject.SINGLETON_ID,
                enabled = equalizer.enabled,
                currentPreset = equalizer.currentPreset,
            )
            equalizerDao.upsertEqualizer(equalizerRow)
            equalizerDao.clearBands()
            equalizerDao.clearPresets()
            if (equalizer.bands.isNotEmpty()) {
                equalizerDao.insertBands(
                    equalizer.bands.mapIndexed { index, band ->
                        band.copy(
                            rowId = 0,
                            equalizerId = EqualizerCacheObject.SINGLETON_ID,
                            position = index,
                        )
                    }
                )
            }
            if (equalizer.presets.isNotEmpty()) {
                equalizerDao.insertPresets(
                    equalizer.presets.mapIndexed { index, preset ->
                        EqualizerPresetEntity.from(preset, position = index)
                    }
                )
            }
        }
    }

    suspend fun getEqualizer(): EqualizerCacheObject? =
        equalizerDao.get()?.toCacheObject()
}
