package pw.vintr.music.domain.playlist.model

import pw.vintr.music.data.playlist.dto.record.PlaylistRecordDto
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel

data class PlaylistRecordModel(
    val id: String,
    val playlistId: String,
    val ordinal: Int,
    val track: TrackModel
)

fun PlaylistRecordDto.toModel() = PlaylistRecordModel(
    id = id,
    playlistId = playlistId,
    ordinal = ordinal,
    track = track.toModel(),
)
