package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.user.dto.register.RegisterRequestDto
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.model.toModel

class RegisterUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        login: String,
        password: String,
        firstName: String,
        lastName: String
    ): UserModel {
        val authResponse = userRepository
            .register(RegisterRequestDto(login, password, firstName, lastName))

        userRepository.setAccessToken(authResponse.token)
        userRepository.setUserId(authResponse.user.id)

        return authResponse.user.toModel()
    }
}
