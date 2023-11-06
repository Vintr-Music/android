package pw.vintr.music.data.library.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import pw.vintr.music.data.library.dto.track.TrackDto

class TrackRemoteDataSource(private val client: HttpClient) {

    suspend fun getTracksByAlbum(
        artist: String,
        album: String,
    ): List<TrackDto> = client.get {
        url(urlString = "api/library/tracks/by-album")
        parameter("artist", artist)
        parameter("album", album)
    }.body()
}
