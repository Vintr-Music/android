package pw.vintr.music.data.player.source

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject

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

    suspend fun removePlayerSession() {
        realm.write { delete(query<PlayerSessionCacheObject>().find()) }
    }
}
