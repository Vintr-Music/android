package pw.vintr.music.data.playlist.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import pw.vintr.music.data.playlist.dto.PlaylistCreateDto
import pw.vintr.music.data.playlist.dto.PlaylistDto
import pw.vintr.music.data.playlist.dto.PlaylistRecordCreateDto
import pw.vintr.music.data.playlist.dto.PlaylistRecordDto

class PlaylistRemoteDataSource(private val client: HttpClient) {

    suspend fun getPlaylists(): List<PlaylistDto> = client.get {
        url("api/playlist/list")
    }.body()

    suspend fun createPlaylist(dto: PlaylistCreateDto): PlaylistDto = client.post {
        url("api/playlist")
        setBody(dto)
    }.body()

    suspend fun removePlaylist(playlistId: String) = client.delete {
        url("api/playlist")
        parameter("id", playlistId)
    }

    suspend fun getPlaylistTracks(playlistId: String): List<PlaylistRecordDto> = client.get {
        url("api/playlist/tracks")
        parameter("id", playlistId)
    }.body()

    suspend fun addPlaylistTrack(dto: PlaylistRecordCreateDto): PlaylistRecordDto = client.post {
        url("api/playlist/tracks")
        setBody(dto)
    }.body()

    suspend fun removePlaylistTrack(playlistId: String, recordId: String) = client.delete {
        url("api/playlist/tracks")
        parameter("playlistId", playlistId)
        parameter("recordId", recordId)
    }
}
