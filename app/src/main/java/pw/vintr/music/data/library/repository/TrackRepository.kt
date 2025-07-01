package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.source.TrackRemoteDataSource

class TrackRepository(
    private val remoteDataSource: TrackRemoteDataSource
) {
    suspend fun getShuffledTracksPage(
        flowSessionId: String,
        offset: Int,
        limit: Int,
    ) = remoteDataSource.getShuffledTracksPage(
        flowSessionId = flowSessionId,
        offset = offset,
        limit = limit
    )

    suspend fun getTracksByAlbum(
        artist: String,
        album: String,
    ) = remoteDataSource.getTracksByAlbum(artist, album)
}
