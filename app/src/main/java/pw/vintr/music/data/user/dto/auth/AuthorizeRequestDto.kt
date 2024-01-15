package pw.vintr.music.data.user.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthorizeRequestDto(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
)
