package pw.vintr.music.domain.server.useCase

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.model.toModel

class GetServerListUseCase(
    private val serverRepository: ServerRepository
) {

    suspend operator fun invoke(): List<ServerModel> {
        return serverRepository
            .getServerList()
            .map { it.toModel() }
    }
}
