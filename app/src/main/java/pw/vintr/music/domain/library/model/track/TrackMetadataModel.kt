package pw.vintr.music.domain.library.model.track

import pw.vintr.music.data.library.dto.track.TrackMetadataDto
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space

data class TrackMetadataModel(
    val album: String,
    val artists: List<String>,
    val genre: List<String>,
    val title: String,
    val number: Int,
    val year: Int
) {
    val artist = artists.joinToString(separator = String.Comma + String.Space)
}

fun TrackMetadataDto.toModel() = TrackMetadataModel(
    album = album,
    artists = artists,
    genre = genre,
    title = title,
    number = track.no,
    year = year,
)
