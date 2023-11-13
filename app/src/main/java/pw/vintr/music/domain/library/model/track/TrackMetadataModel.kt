package pw.vintr.music.domain.library.model.track

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.library.dto.track.TrackMetadataDto
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.tools.extension.Comma
import pw.vintr.music.tools.extension.Space

@Parcelize
data class TrackMetadataModel(
    val album: String,
    val artists: List<ArtistModel>,
    val genre: List<String>,
    val title: String,
    val number: Int,
    val year: Int?
) : Parcelable {

    @IgnoredOnParcel
    val artist = artists.joinToString(separator = String.Comma + String.Space) { it.name }
}

fun TrackMetadataDto.toModel() = TrackMetadataModel(
    album = album.orEmpty(),
    artists = artists?.map { ArtistModel(it) }.orEmpty(),
    genre = genre.orEmpty(),
    title = title.orEmpty(),
    number = track.no ?: 0,
    year = year,
)
