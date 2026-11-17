package pw.vintr.music.data.equalizer.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EqualizerDao {

    @Transaction
    @Query(
        """
        SELECT * FROM equalizer
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun get(id: Int = EqualizerCacheObject.SINGLETON_ID): EqualizerWithChildren?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEqualizer(equalizer: EqualizerCacheObject)

    @Insert
    suspend fun insertBands(bands: List<BandCacheObject>)

    @Insert
    suspend fun insertPresets(presets: List<EqualizerPresetEntity>)

    @Query(
        """
        DELETE FROM equalizer_band
        WHERE equalizerId = :equalizerId
        """
    )
    suspend fun clearBands(equalizerId: Int = EqualizerCacheObject.SINGLETON_ID)

    @Query(
        """
        DELETE FROM equalizer_preset
        WHERE equalizerId = :equalizerId
        """
    )
    suspend fun clearPresets(equalizerId: Int = EqualizerCacheObject.SINGLETON_ID)
}
