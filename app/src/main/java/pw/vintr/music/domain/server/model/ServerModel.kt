package pw.vintr.music.domain.server.model

import pw.vintr.music.data.server.dto.ServerDto

data class ServerModel(
    val id: String,
    val name: String,
    val owner: String,
    val users: List<String>,
    val haveAccessControl: Boolean
)

fun ServerDto.toModel(
    haveAccessControl: Boolean = false
) = ServerModel(id, name, owner, users, haveAccessControl)
