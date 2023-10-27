package pw.vintr.music.data.library.dto.album

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("artist")
    val artist: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("year")
    val year: Int?
)
