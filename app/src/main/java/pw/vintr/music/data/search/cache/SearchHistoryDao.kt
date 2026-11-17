package pw.vintr.music.data.search.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query(
        """
        SELECT * FROM search_history
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observe(id: Int = SearchHistoryCacheObject.SINGLETON_ID): Flow<SearchHistoryCacheObject?>

    @Query(
        """
        SELECT * FROM search_history
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun get(id: Int = SearchHistoryCacheObject.SINGLETON_ID): SearchHistoryCacheObject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(history: SearchHistoryCacheObject)
}
