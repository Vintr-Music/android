package pw.vintr.music.data.library.repository

import pw.vintr.music.data.library.source.SearchRemoteDataSource

class SearchRepository(
    private val searchRemoteDataSource: SearchRemoteDataSource
) {

    suspend fun searchLibrary(
        query: String,
        limit: Int,
        offset: Int
    ) = searchRemoteDataSource.getSearchResults(query, limit, offset)

    suspend fun searchTracks(
        query: String,
        limit: Int,
        offset: Int
    ) = searchRemoteDataSource.getTracksSearchResults(query, limit, offset)
}
