package pw.vintr.music.data.server.repository

import pw.vintr.music.data.server.source.ServerRemoteDataSource

class ServerRepository(
    private val remoteDataSource: ServerRemoteDataSource
) {

    suspend fun getServerList() = remoteDataSource.getServerList()
}
