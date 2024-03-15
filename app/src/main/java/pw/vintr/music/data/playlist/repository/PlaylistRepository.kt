package pw.vintr.music.data.playlist.repository

import pw.vintr.music.data.playlist.dto.PlaylistCreateDto
import pw.vintr.music.data.playlist.dto.PlaylistUpdateDto
import pw.vintr.music.data.playlist.dto.record.PlaylistRecordCreateDto
import pw.vintr.music.data.playlist.dto.PlaylistUpdateRecordsDto
import pw.vintr.music.data.playlist.source.PlaylistRemoteDataSource

class PlaylistRepository(private val remoteDataSource: PlaylistRemoteDataSource) {

    suspend fun getPlaylists(containsTrackId: String? = null) = remoteDataSource
        .getPlaylists(containsTrackId)

    suspend fun getPlaylistById(playlistId: String) = remoteDataSource
        .getPlaylistById(playlistId)

    suspend fun createPlaylist(dto: PlaylistCreateDto) = remoteDataSource
        .createPlaylist(dto)

    suspend fun updatePlaylist(dto: PlaylistUpdateDto) = remoteDataSource
        .updatePlaylist(dto)

    suspend fun removePlaylist(playlistId: String) = remoteDataSource
        .removePlaylist(playlistId)

    suspend fun getPlaylistTracks(playlistId: String) = remoteDataSource
        .getPlaylistTracks(playlistId)

    suspend fun addPlaylistTrack(dto: PlaylistRecordCreateDto) = remoteDataSource
        .addPlaylistTrack(dto)

    suspend fun removePlaylistTrack(playlistId: String, recordId: String) = remoteDataSource
        .removePlaylistTrack(playlistId, recordId)

    suspend fun updatePlaylistTracks(dto: PlaylistUpdateRecordsDto) = remoteDataSource
        .updatePlaylistTracks(dto)
}
