package pw.vintr.music.domain.library.model.track

import pw.vintr.music.data.library.dto.track.TrackMetadataDto

data class TrackMetadataModel(
    val album: String,
    val artists: List<String>,
    val genre: List<String>,
    val title: String,
    val number: Int,
    val year: Int
)

fun TrackMetadataDto.toModel() = TrackMetadataModel(
    album = album,
    artists = artists,
    genre = genre,
    title = title,
    number = track.no,
    year = year,
)
