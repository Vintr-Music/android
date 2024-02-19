package pw.vintr.music.domain.user.useCase

import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.model.toModel

class GetProfileUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): UserModel = userRepository
        .getProfile()
        .toModel()
        .also { userRepository.setUserId(it.id) }
}
