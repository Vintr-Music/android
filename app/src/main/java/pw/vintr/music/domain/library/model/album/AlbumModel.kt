package pw.vintr.music.domain.library.model.album

import pw.vintr.music.data.library.dto.album.AlbumDto
import pw.vintr.music.BuildConfig

data class AlbumModel(
    val artist: String,
    val name: String,
    val year: Int?,
    val artworkUrl: String,
)

fun AlbumDto.toModel() = AlbumModel(
    artist = artist,
    name = name,
    year = year,
    artworkUrl = BuildConfig.BASE_URL + "api/library/artworks/album?artist=$artist&album=$name"
)
