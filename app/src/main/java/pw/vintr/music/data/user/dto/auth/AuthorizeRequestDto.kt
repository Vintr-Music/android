package pw.vintr.music.data.user.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthorizeRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
