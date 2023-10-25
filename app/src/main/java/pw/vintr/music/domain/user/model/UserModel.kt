package pw.vintr.music.domain.user.model

import pw.vintr.music.data.user.dto.UserDto

data class UserModel(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
)

fun UserDto.toModel() = UserModel(id, email, firstName, lastName)
