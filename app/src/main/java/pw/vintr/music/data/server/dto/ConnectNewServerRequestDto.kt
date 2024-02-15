package pw.vintr.music.data.server.dto

import com.google.gson.annotations.SerializedName

data class ConnectNewServerRequestDto(
    @SerializedName("serverName")
    val serverName: String,
    @SerializedName("code")
    val code: String,
)
