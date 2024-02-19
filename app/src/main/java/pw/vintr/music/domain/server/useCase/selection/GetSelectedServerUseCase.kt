package pw.vintr.music.domain.server.useCase.selection

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.exception.ServerNotSelectedException
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.model.toModel

class GetSelectedServerUseCase(
    private val serverRepository: ServerRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): ServerModel {
        val currentUserId = userRepository.getUserId()
        val serverDto = serverRepository.getSelectedServerId()
            ?.let { serverRepository.getServerById(it) }

        return serverDto?.toModel(
            haveAccessControl = serverDto.owner == currentUserId
        ) ?: throw ServerNotSelectedException
    }
}
