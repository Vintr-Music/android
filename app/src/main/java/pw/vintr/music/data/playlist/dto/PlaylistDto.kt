package pw.vintr.music.data.playlist.dto

import com.google.gson.annotations.SerializedName

data class PlaylistDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("createdAt")
    val createdAt: Long,
)
