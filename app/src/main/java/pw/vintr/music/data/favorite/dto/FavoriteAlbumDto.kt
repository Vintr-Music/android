package pw.vintr.music.data.favorite.dto

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.library.dto.album.AlbumDto

data class FavoriteAlbumDto(
    @SerializedName("album")
    val album: AlbumDto,
)
