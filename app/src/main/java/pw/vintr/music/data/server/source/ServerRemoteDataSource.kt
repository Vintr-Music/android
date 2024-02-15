package pw.vintr.music.data.server.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import pw.vintr.music.data.server.dto.ConnectNewServerRequestDto
import pw.vintr.music.data.server.dto.ServerDto

class ServerRemoteDataSource(private val client: HttpClient) {

    suspend fun getServerList(): List<ServerDto> = client.get {
        url("gate/server/list")
    }.body()

    suspend fun getServerById(id: String): ServerDto = client.get {
        url("gate/server/by-id")
        parameter("id", id)
    }.body()

    suspend fun connectNewServer(
        requestDto: ConnectNewServerRequestDto
    ): ServerDto = client.patch {
        url("gate/invite/use")
        setBody(requestDto)
    }.body()
}
