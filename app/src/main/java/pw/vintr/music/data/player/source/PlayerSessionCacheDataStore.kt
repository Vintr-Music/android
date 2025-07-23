package pw.vintr.music.data.player.source

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlayerSessionCacheDataStore(private val realm: Realm) {

    suspend fun savePlayerSession(session: PlayerSessionCacheObject) {
        realm.write {
            // Find possibly existing session
            val previousSession = query<PlayerSessionCacheObject>()
                .first()
                .find()

            previousSession?.let {
                // Update existing session
                it.album = session.album
                it.artist = session.artist
                it.playlistId = session.playlistId
                it.sessionId = session.sessionId
                it.totalCount = session.totalCount
                it.tracks = session.tracks
            } ?: run {
                // Save new session
                copyToRealm(session)
            }
        }
    }

    fun getPlayerSessionFlow() = realm.query<PlayerSessionCacheObject>()
        .first()
        .asFlow()
        .map { it.obj }

    suspend fun getPlayerSession(): PlayerSessionCacheObject? {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val session = realm.query<PlayerSessionCacheObject>()
                    .first()
                    .find()
                    ?.copyFromRealm()

                continuation.resume(session)
            }
        }
    }

    suspend fun removePlayerSession() {
        realm.write { delete(query<PlayerSessionCacheObject>().find()) }
    }
}
