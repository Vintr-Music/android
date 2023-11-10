package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.source.ArtistRemoteDataSource

class ArtistRepository(
    private val remoteDataSource: ArtistRemoteDataSource
) {
    suspend fun getArtistList() = remoteDataSource.getArtistList()
}
