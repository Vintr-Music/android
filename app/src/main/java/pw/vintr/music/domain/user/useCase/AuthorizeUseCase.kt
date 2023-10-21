package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.user.dto.UserDto
import pw.vintr.music.data.user.dto.auth.AuthorizeRequestDto
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.user.model.UserModel

class AuthorizeUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String, password: String): UserModel {
        return userRepository
            .authorize(AuthorizeRequestDto(email, password))
            .user
            .toModel()
    }

    private fun UserDto.toModel() = UserModel(id, email, firstName, lastName)
}
