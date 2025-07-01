package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.dto.track.TrackDto
import pw.vintr.music.data.library.source.TrackRemoteDataSource
import pw.vintr.music.data.pagination.PageDto

class TrackRepository(
    private val remoteDataSource: TrackRemoteDataSource
) {
    suspend fun getTracksPage(
        urlString: String,
        params: Map<String, Any>,
    ): PageDto<TrackDto> = remoteDataSource.getTracksPage(urlString, params)

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
