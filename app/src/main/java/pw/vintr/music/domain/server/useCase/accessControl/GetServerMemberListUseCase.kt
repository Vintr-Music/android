package pw.vintr.music.domain.server.useCase.accessControl

import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.domain.user.model.toModel

class GetServerMemberListUseCase(private val serverRepository: ServerRepository) {

    suspend operator fun invoke(serverId: String): List<UserModel> {
        return serverRepository
            .getServerMembers(serverId)
            .map { it.toModel() }
    }
}
