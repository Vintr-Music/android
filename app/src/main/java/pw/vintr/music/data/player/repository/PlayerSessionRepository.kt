package pw.vintr.music.data.player.repository

import pw.vintr.music.data.player.dao.PlayerSessionCacheObject
import pw.vintr.music.data.player.source.PlayerSessionCacheDataStore

class PlayerSessionRepository(
    private val playerSessionCacheDataStore: PlayerSessionCacheDataStore,
) {

    suspend fun savePlayerSession(
        session: PlayerSessionCacheObject
    ) = playerSessionCacheDataStore.savePlayerSession(session)

    fun getPlayerSessionFlow() = playerSessionCacheDataStore.getPlayerSessionFlow()
}
