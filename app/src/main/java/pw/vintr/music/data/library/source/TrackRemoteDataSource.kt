package pw.vintr.music.data.library.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import pw.vintr.music.data.library.dto.track.TrackDto
import pw.vintr.music.tools.extension.encodedParameter
import pw.vintr.music.tools.extension.urlEncode

class TrackRemoteDataSource(private val client: HttpClient) {

    suspend fun getTracksByAlbum(
        artist: String,
        album: String,
    ): List<TrackDto> = client.get {
        url(urlString = "api/library/tracks/by-album")
        encodedParameter("artist", artist.urlEncode())
        encodedParameter("album", album.urlEncode())
    }.body()
}
