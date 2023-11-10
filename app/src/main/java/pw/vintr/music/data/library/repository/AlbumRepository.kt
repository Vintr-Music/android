package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.source.AlbumRemoteDataSource

class AlbumRepository(
    private val albumRemoteDataSource: AlbumRemoteDataSource
) {

    suspend fun getAlbumsByArtist(
        artist: String
    ) = albumRemoteDataSource.getAlbumsByArtist(artist)
}
