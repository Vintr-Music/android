package pw.vintr.music.data.player.source

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map
import pw.vintr.music.data.player.dao.PlayerSessionCacheObject

class PlayerSessionCacheDataStore(private val realm: Realm) {

    suspend fun savePlayerSession(session: PlayerSessionCacheObject) {
        realm.write {
            // Remove previous session
            query<PlayerSessionCacheObject>()
                .find()
                .let { delete(it) }

            // Save current session
            copyToRealm(session)
        }
    }

    fun getPlayerSessionFlow() = realm.query<PlayerSessionCacheObject>()
        .first()
        .asFlow()
        .map { it.obj }
}
