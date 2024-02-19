package pw.vintr.music.domain.server.useCase.accessControl

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.domain.server.model.toModel

class GetServerInviteListUseCase(private val serverRepository: ServerRepository) {

    suspend operator fun invoke(serverId: String): List<ServerInviteModel> {
        return serverRepository
            .getServerInvites(serverId)
            .map { it.toModel() }
    }
}
