package pw.vintr.music.data.server.dto

import com.google.gson.annotations.SerializedName

data class ServerDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("users")
    val users: List<String>,
)
