package pw.vintr.music.domain.server.model

import pw.vintr.music.data.server.dto.ServerDto

data class ServerModel(
    val id: String,
    val name: String,
    val owner: String,
    val users: List<String>,
)

fun ServerDto.toModel() = ServerModel(id, name, owner, users)
