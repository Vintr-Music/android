package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.data.user.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository,
    private val serverRepository: ServerRepository
) {

    operator fun invoke() {
        userRepository.removeAccessToken()
        userRepository.removeUserId()
        serverRepository.removeSelectedServerId()
    }
}
