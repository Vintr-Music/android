package pw.vintr.music.domain.server.useCase.selection

import pw.vintr.music.data.server.repository.ServerRepository

class GetIsServerSelectedUseCase(private val serverRepository: ServerRepository) {
    operator fun invoke() = serverRepository.getSelectedServerId().isNullOrBlank().not()
}
