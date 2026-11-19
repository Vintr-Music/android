package pw.vintr.music.data.player.source

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pw.vintr.music.data.cache.db.CacheDatabase
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.data.player.cache.PlayerSessionDao

/**
 * Public API intentionally unchanged vs the Realm implementation:
 * - suspend savePlayerSession(PlayerSessionCacheObject)
 * - Flow&lt;PlayerSessionCacheObject?&gt; getPlayerSessionFlow()
 * - suspend getPlayerSession()
 * - suspend removePlayerSession()
 */
class PlayerSessionCacheDataStore(
    private val database: CacheDatabase,
    private val playerSessionDao: PlayerSessionDao,
) {

    suspend fun savePlayerSession(session: PlayerSessionCacheObject) {
        database.withTransaction {
            val sessionRow = session.copy(id = PlayerSessionCacheObject.SINGLETON_ID)
            playerSessionDao.upsertSession(sessionRow)
            playerSessionDao.clearTracks()
            if (session.tracks.isNotEmpty()) {
                playerSessionDao.insertTracks(
                    session.tracks.mapIndexed { index, track ->
                        track.copy(
                            rowId = 0,
                            sessionId = PlayerSessionCacheObject.SINGLETON_ID,
                            position = index,
                        )
                    }
                )
            }
        }
    }

    fun getPlayerSessionFlow(): Flow<PlayerSessionCacheObject?> =
        playerSessionDao.observe().map { it?.toCacheObject() }

    suspend fun getPlayerSession(): PlayerSessionCacheObject? =
        playerSessionDao.get()?.toCacheObject()

    suspend fun removePlayerSession() {
        database.withTransaction {
            playerSessionDao.clearTracks()
            playerSessionDao.clearSession()
        }
    }
}
