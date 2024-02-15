package pw.vintr.music.domain.server.useCase.selection

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.server.model.ServerModel

class SelectServerUseCase(
    private val serverRepository: ServerRepository
) {

    operator fun invoke(server: ServerModel) {
        serverRepository.setSelectedServerId(server.id)
    }
}
