package pw.vintr.music.domain.playlist.interactor

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import pw.vintr.music.data.playlist.dto.PlaylistCreateDto
import pw.vintr.music.data.playlist.dto.record.PlaylistRecordCreateDto
import pw.vintr.music.data.playlist.dto.record.PlaylistRecordUpdateDto
import pw.vintr.music.data.playlist.dto.PlaylistUpdateDto
import pw.vintr.music.data.playlist.repository.PlaylistRepository
import pw.vintr.music.domain.base.BaseDomainState
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.domain.playlist.model.PlaylistRecordModel
import pw.vintr.music.domain.playlist.model.toModel
import pw.vintr.music.tools.extension.updateLoaded

class PlaylistInteractor(
    private val playlistRepository: PlaylistRepository,
) : BaseInteractor() {

    sealed class Event {

        abstract val playlistId: String

        data class AddedTrack(
            override val playlistId: String,
            val record: PlaylistRecordModel
        ) : Event()

        data class RemovedTrack(
            override val playlistId: String,
            val record: PlaylistRecordModel
        ) : Event()

        data class UpdatedTracks(
            override val playlistId: String,
            val records: List<PlaylistRecordModel>
        ) : Event()
    }

    private val _dataFlow: MutableStateFlow<BaseDomainState<List<PlaylistModel>>> =
        MutableStateFlow(value = BaseDomainState.Loading())
    val dataFlow = _dataFlow.shareIn(
        scope = this,
        started = SharingStarted.Lazily,
        replay = 1
    )

    private val _events = Channel<Event>()
    val events by lazy {
        _events
            .receiveAsFlow()
            .shareIn(scope = this, started = SharingStarted.Lazily)
    }

    suspend fun loadPlaylists() {
        _dataFlow.loadWithStateHandling { getPlaylists() }
    }

    suspend fun refreshPlaylists() {
        _dataFlow.refreshWithStateHandling { getPlaylists() }
    }

    private suspend fun getPlaylists() = playlistRepository
        .getPlaylists()
        .map { it.toModel() }

    suspend fun getPlaylistById(playlistId: String) = playlistRepository
        .getPlaylistById(playlistId = playlistId)
        .toModel()

    suspend fun getPlaylistsWithTrack(trackId: String) = playlistRepository
        .getPlaylists(containsTrackId = trackId)
        .map { it.toModel() }

    suspend fun createPlaylist(
        name: String,
        description: String,
    ) {
        val newPlaylist = playlistRepository.createPlaylist(
            PlaylistCreateDto(
                name = name,
                description = description,
            )
        ).toModel()

        _dataFlow.updateLoaded { playlists ->
            listOf(newPlaylist, *playlists.toTypedArray())
        }
    }

    suspend fun removePlaylist(playlistId: String) {
        playlistRepository.removePlaylist(playlistId)

        _dataFlow.updateLoaded { playlists ->
            playlists.filter { it.id != playlistId }
        }
    }

    suspend fun getPlaylistTracks(playlistId: String) = playlistRepository
        .getPlaylistTracks(playlistId)
        .map { it.toModel() }

    suspend fun addTrackToPlaylist(
        playlistId: String,
        trackId: String
    ) {
        val newTrack = playlistRepository.addPlaylistTrack(
            PlaylistRecordCreateDto(
                playlistId = playlistId,
                trackId = trackId,
            )
        ).toModel()

        _events.trySend(Event.AddedTrack(playlistId, newTrack))
    }

    suspend fun removeTrackFromPlaylist(record: PlaylistRecordModel) {
        playlistRepository.removePlaylistTrack(
            playlistId = record.playlistId,
            recordId = record.id,
        )

        _events.trySend(Event.RemovedTrack(record.playlistId, record))
    }

    suspend fun updateTracks(
        playlistId: String,
        newRecords: List<PlaylistRecordModel>
    ) {
        val updatedRecords = playlistRepository.updatePlaylistTracks(
            PlaylistUpdateDto(
                playlistId = playlistId,
                updateData = newRecords.map { model ->
                    PlaylistRecordUpdateDto(
                        trackId = model.track.md5,
                        ordinal = model.ordinal,
                    )
                }
            )
        ).map { it.toModel() }

        _events.trySend(Event.UpdatedTracks(playlistId, updatedRecords))
    }
}
