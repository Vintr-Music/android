package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.user.repository.UserRepository

class GetAuthorizeStateUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = !userRepository.getAccessToken().isNullOrBlank()
}
