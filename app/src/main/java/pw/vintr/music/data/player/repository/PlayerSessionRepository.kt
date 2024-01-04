package pw.vintr.music.data.player.repository

import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.data.player.source.PlayerSessionCacheDataStore

class PlayerSessionRepository(
    private val playerSessionCacheDataStore: PlayerSessionCacheDataStore,
) {

    suspend fun savePlayerSession(
        session: PlayerSessionCacheObject
    ) = playerSessionCacheDataStore.savePlayerSession(session)

    fun getPlayerSessionFlow() = playerSessionCacheDataStore.getPlayerSessionFlow()

    suspend fun removePlayerSession() = playerSessionCacheDataStore.removePlayerSession()
}
