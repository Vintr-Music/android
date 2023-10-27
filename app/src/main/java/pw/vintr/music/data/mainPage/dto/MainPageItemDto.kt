package pw.vintr.music.data.mainPage.dto

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.library.dto.album.AlbumDto

data class MainPageItemDto(
    @SerializedName("artist")
    val artist: String,
    @SerializedName("albums")
    val albums: List<AlbumDto>
)
