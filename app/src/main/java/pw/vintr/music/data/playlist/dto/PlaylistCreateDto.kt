package pw.vintr.music.data.playlist.dto

import com.google.gson.annotations.SerializedName

data class PlaylistCreateDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
)
