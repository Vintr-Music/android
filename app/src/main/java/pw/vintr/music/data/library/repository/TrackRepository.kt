package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.source.TrackRemoteDataSource

class TrackRepository(
    private val remoteDataSource: TrackRemoteDataSource
) {

    suspend fun getTracksByAlbum(
        artist: String,
        album: String,
    ) = remoteDataSource.getTracksByAlbum(artist, album)
}
