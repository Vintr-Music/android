package pw.vintr.music.data.user.dto.auth

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.user.dto.UserDto

data class AuthorizeResponseDto(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("token")
    val token: String
)
