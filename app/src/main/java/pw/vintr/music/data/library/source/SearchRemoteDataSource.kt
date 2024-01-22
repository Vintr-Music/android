package pw.vintr.music.data.library.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import pw.vintr.music.data.library.dto.search.SearchContentDto
import pw.vintr.music.data.library.dto.track.TrackDto
import pw.vintr.music.data.pagination.PageDto

class SearchRemoteDataSource(private val client: HttpClient) {

    suspend fun getSearchResults(
        query: String,
        limit: Int,
        offset: Int
    ): SearchContentDto = client.get {
        url(urlString = "api/library/search")
        parameter("query", query)
        parameter("limit", limit)
        parameter("offset", offset)
    }.body()

    suspend fun getTracksSearchResults(
        query: String,
        limit: Int,
        offset: Int
    ): PageDto<TrackDto> = client.get {
        url(urlString = "api/library/search/tracks")
        parameter("query", query)
        parameter("limit", limit)
        parameter("offset", offset)
    }.body()
}
