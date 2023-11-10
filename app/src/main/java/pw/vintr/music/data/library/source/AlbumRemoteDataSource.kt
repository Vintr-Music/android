package pw.vintr.music.data.library.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import pw.vintr.music.data.library.dto.album.AlbumDto

class AlbumRemoteDataSource(private val client: HttpClient) {

    suspend fun getAlbumsByArtist(artist: String): List<AlbumDto> = client.get {
        url(urlString = "api/library/albums/by-artist")
        parameter("artist", artist)
    }.body()
}
