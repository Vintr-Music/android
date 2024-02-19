package pw.vintr.music.domain.server.useCase.list

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.model.toModel

class GetServerListUseCase(
    private val serverRepository: ServerRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): List<ServerModel> {
        val currentUserId = userRepository.getUserId()

        return serverRepository
            .getServerList()
            .map { it.toModel(haveAccessControl = it.owner == currentUserId) }
    }
}
