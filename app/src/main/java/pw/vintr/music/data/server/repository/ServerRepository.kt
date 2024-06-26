package pw.vintr.music.data.server.repository

import pw.vintr.music.data.server.dto.ConnectNewServerRequestDto
import pw.vintr.music.data.server.source.ServerPreferencesDataSource
import pw.vintr.music.data.server.source.ServerRemoteDataSource

class ServerRepository(
    private val remoteDataSource: ServerRemoteDataSource,
    private val preferencesDataSource: ServerPreferencesDataSource,
) {

    suspend fun getServerList() = remoteDataSource.getServerList()

    suspend fun getServerById(id: String) = remoteDataSource.getServerById(id)

    fun setSelectedServerId(serverId: String) = preferencesDataSource
        .setSelectedServerId(serverId)

    fun getSelectedServerId() = preferencesDataSource.getSelectedServerId()

    fun removeSelectedServerId() = preferencesDataSource.removeSelectedServerId()

    suspend fun connectNewServer(requestDto: ConnectNewServerRequestDto) = remoteDataSource
        .connectNewServer(requestDto)

    suspend fun getServerInvites(serverId: String) = remoteDataSource
        .getServerInvites(serverId)

    suspend fun getServerMembers(serverId: String) = remoteDataSource
        .getServerMembers(serverId)
}
