package pw.vintr.music.data.server.repository

import pw.vintr.music.data.server.source.ServerPreferencesDataSource
import pw.vintr.music.data.server.source.ServerRemoteDataSource

class ServerRepository(
    private val remoteDataSource: ServerRemoteDataSource,
    private val preferencesDataSource: ServerPreferencesDataSource,
) {

    suspend fun getServerList() = remoteDataSource.getServerList()

    fun setSelectedServerId(serverId: String) = preferencesDataSource.setSelectedServerId(serverId)

    fun getSelectedServerId() = preferencesDataSource.getSelectedServerId()
}
