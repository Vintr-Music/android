package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.user.dto.auth.AuthorizeRequestDto
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.model.toModel

class AuthorizeUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(login: String, password: String): UserModel {
        val authResponse = userRepository
            .authorize(AuthorizeRequestDto(login, password))

        userRepository.setAccessToken(authResponse.token)
        userRepository.setUserId(authResponse.user.id)

        return authResponse.user.toModel()
    }
}
