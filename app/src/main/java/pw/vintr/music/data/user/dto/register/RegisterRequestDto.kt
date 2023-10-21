package pw.vintr.music.data.user.dto.register

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("password")
    val password: String,
)
