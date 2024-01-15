package pw.vintr.music.domain.user.model

import pw.vintr.music.data.user.dto.UserDto

data class UserModel(
    val id: String,
    val login: String,
    val firstName: String,
    val lastName: String,
) {
    val fullName = "$firstName $lastName"
}

fun UserDto.toModel() = UserModel(id, login, firstName, lastName)
