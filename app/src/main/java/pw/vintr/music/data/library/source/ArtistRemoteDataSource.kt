package pw.vintr.music.data.library.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class ArtistRemoteDataSource(private val client: HttpClient) {

    suspend fun getArtistList(): List<String> = client.get {
        url(urlString = "api/library/artists")
    }.body()
}
