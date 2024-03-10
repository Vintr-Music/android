package pw.vintr.music.data.playlist.repository

import pw.vintr.music.data.playlist.dto.PlaylistCreateDto
import pw.vintr.music.data.playlist.dto.PlaylistRecordCreateDto
import pw.vintr.music.data.playlist.source.PlaylistRemoteDataSource

class PlaylistRepository(private val remoteDataSource: PlaylistRemoteDataSource) {

    suspend fun getPlaylists() = remoteDataSource.getPlaylists()

    suspend fun createPlaylist(dto: PlaylistCreateDto) = remoteDataSource
        .createPlaylist(dto)

    suspend fun removePlaylist(playlistId: String) = remoteDataSource
        .removePlaylist(playlistId)

    suspend fun getPlaylistTracks(playlistId: String) = remoteDataSource
        .getPlaylistTracks(playlistId)

    suspend fun addPlaylistTrack(dto: PlaylistRecordCreateDto) = remoteDataSource
        .addPlaylistTrack(dto)

    suspend fun removePlaylistTrack(playlistId: String, recordId: String) = remoteDataSource
        .removePlaylistTrack(playlistId, recordId)
}
