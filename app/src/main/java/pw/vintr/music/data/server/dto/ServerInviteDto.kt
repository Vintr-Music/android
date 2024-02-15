package pw.vintr.music.data.server.dto

import com.google.gson.annotations.SerializedName

data class ServerInviteDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("server")
    val server: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("isUnlimited")
    val isUnlimited: Boolean,
    @SerializedName("expiry")
    val expiry: String?
)
