package pw.vintr.music.domain.library.model.album

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.library.dto.album.AlbumDto
import pw.vintr.music.BuildConfig
import pw.vintr.music.data.library.cache.album.AlbumCacheObject
import pw.vintr.music.domain.library.model.artist.ArtistModel

@Parcelize
data class AlbumModel(
    val artist: ArtistModel,
    val name: String,
    val year: Int?,
) : Parcelable {

    @IgnoredOnParcel
    val artworkUrl = BuildConfig.BASE_URL +
            "api/library/artworks/album?artist=${artist.name}&album=$name"

    fun toCacheObject() = AlbumCacheObject(
        artist = artist.name,
        name = name,
        year = year,
    )
}

fun AlbumDto.toModel() = AlbumModel(
    artist = ArtistModel(artist),
    name = name.orEmpty(),
    year = year,
)

fun AlbumCacheObject.toModel() = AlbumModel(
    artist = ArtistModel(artist),
    name = name.orEmpty(),
    year = year,
)
