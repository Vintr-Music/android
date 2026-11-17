package pw.vintr.music.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pw.vintr.music.data.cache.db.converter.StringListConverter
import pw.vintr.music.data.equalizer.cache.BandCacheObject
import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import pw.vintr.music.data.equalizer.cache.EqualizerDao
import pw.vintr.music.data.equalizer.cache.EqualizerPresetEntity
import pw.vintr.music.data.library.cache.track.TrackCacheObject
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.data.player.cache.PlayerSessionDao
import pw.vintr.music.data.search.cache.SearchHistoryCacheObject
import pw.vintr.music.data.search.cache.SearchHistoryDao

/**
 * Local cache database (Room).
 *
 * Replaces Realm for SearchHistory, PlayerSession and Equalizer.
 * Version stays at 1 until the migration is fully rolled out; schema changes
 * during migration use [fallbackToDestructiveMigration].
 */
@Database(
    entities = [
        SearchHistoryCacheObject::class,
        PlayerSessionCacheObject::class,
        TrackCacheObject::class,
        EqualizerCacheObject::class,
        BandCacheObject::class,
        EqualizerPresetEntity::class,
    ],
    version = CACHE_DATABASE_VERSION,
    exportSchema = false,
)
@TypeConverters(StringListConverter::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun playerSessionDao(): PlayerSessionDao

    abstract fun equalizerDao(): EqualizerDao
}

const val CACHE_DATABASE_NAME = "vintr_cache.db"
const val CACHE_DATABASE_VERSION = 1
