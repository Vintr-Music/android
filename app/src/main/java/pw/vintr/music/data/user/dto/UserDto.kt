package pw.vintr.music.data.user.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
)
