package pw.vintr.music.domain.server.useCase

import pw.vintr.music.data.server.repository.ServerRepository

class GetSelectedServerIdUseCase(private val serverRepository: ServerRepository) {
    operator fun invoke() = serverRepository.getSelectedServerId()
}
