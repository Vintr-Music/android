package pw.vintr.music.domain.library.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.library.dto.album.AlbumDto
import pw.vintr.music.BuildConfig

@Parcelize
data class AlbumModel(
    val artist: String,
    val name: String,
    val year: Int?,
    val artworkUrl: String,
) : Parcelable

fun AlbumDto.toModel() = AlbumModel(
    artist = artist,
    name = name.orEmpty(),
    year = year,
    artworkUrl = BuildConfig.BASE_URL + "api/library/artworks/album?artist=$artist&album=$name"
)
