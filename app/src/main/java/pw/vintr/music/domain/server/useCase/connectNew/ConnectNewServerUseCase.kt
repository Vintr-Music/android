package pw.vintr.music.domain.server.useCase.connectNew

import com.google.gson.Gson
import pw.vintr.music.data.server.dto.ConnectNewServerRequestDto
import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.model.toModel

class ConnectNewServerUseCase(
    private val serverRepository: ServerRepository
) {

    suspend operator fun invoke(serverName: String, inviteCode: String): ServerModel {
        return serverRepository
            .connectNewServer(
                ConnectNewServerRequestDto(
                    serverName = serverName,
                    code = inviteCode
                )
            )
            .toModel()
    }

    suspend operator fun invoke(qrData: String): ServerModel {
        val decodedData = Gson()
            .fromJson(qrData, ConnectNewServerRequestDto::class.java)

        return serverRepository
            .connectNewServer(decodedData)
            .toModel()
    }
}
