package pw.vintr.music.domain.server.useCase.selection

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.exception.ServerNotSelectedException
import pw.vintr.music.domain.server.model.toModel

class GetSelectedServerUseCase(private val serverRepository: ServerRepository) {

    suspend operator fun invoke() = serverRepository.getSelectedServerId()
        ?.let { serverRepository.getServerById(it) }
        ?.toModel() ?: throw ServerNotSelectedException
}
