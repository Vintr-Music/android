package pw.vintr.music.data.player.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pw.vintr.music.data.library.cache.track.TrackCacheObject

@Dao
interface PlayerSessionDao {

    @Transaction
    @Query(
        """
        SELECT * FROM player_session
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observe(id: Int = PlayerSessionCacheObject.SINGLETON_ID): Flow<PlayerSessionWithTracks?>

    @Transaction
    @Query(
        """
        SELECT * FROM player_session
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun get(id: Int = PlayerSessionCacheObject.SINGLETON_ID): PlayerSessionWithTracks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: PlayerSessionCacheObject)

    @Insert
    suspend fun insertTracks(tracks: List<TrackCacheObject>)

    @Query(
        """
        DELETE FROM player_session_track
        WHERE sessionId = :sessionId
        """
    )
    suspend fun clearTracks(sessionId: Int = PlayerSessionCacheObject.SINGLETON_ID)

    @Query("DELETE FROM player_session")
    suspend fun clearSession()
}
