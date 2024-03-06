package pw.vintr.music.data.favorite.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import pw.vintr.music.data.favorite.dto.FavoriteAlbumDto
import pw.vintr.music.data.favorite.dto.FavoriteArtistDto
import pw.vintr.music.data.favorite.dto.FavoriteStatusDto
import pw.vintr.music.data.library.dto.album.AlbumDto

class FavoriteRemoteDataSource(private val client: HttpClient) {

    suspend fun getAlbumIsFavorite(album: AlbumDto): FavoriteStatusDto = client.get {
        url(urlString = "api/favorite/albums/is-favorite")
        parameter("name", album.name)
        parameter("year", album.year)
        parameter("artist", album.artist)
    }.body()

    suspend fun getFavoriteAlbums(): List<AlbumDto> = client.get {
        url(urlString = "api/favorite/albums/list")
    }.body()

    suspend fun addAlbumToFavorites(request: FavoriteAlbumDto) {
        client.post {
            url(urlString = "api/favorite/albums/add")
            setBody(request)
        }
    }

    suspend fun removeAlbumFromFavorites(request: FavoriteAlbumDto) {
        client.delete {
            url(urlString = "api/favorite/albums/remove")
            setBody(request)
        }
    }

    suspend fun getArtistIsFavorite(artist: String): FavoriteStatusDto = client.get {
        url(urlString = "api/favorite/artists/is-favorite")
        parameter("artist", artist)
    }.body()

    suspend fun getFavoriteArtists(): List<String> = client.get {
        url(urlString = "api/favorite/artists/list")
    }.body()

    suspend fun addArtistToFavorites(request: FavoriteArtistDto) {
        client.post {
            url(urlString = "api/favorite/artists/add")
            setBody(request)
        }
    }

    suspend fun removeArtistFromFavorites(request: FavoriteArtistDto) {
        client.delete {
            url(urlString = "api/favorite/artists/remove")
            setBody(request)
        }
    }
}
