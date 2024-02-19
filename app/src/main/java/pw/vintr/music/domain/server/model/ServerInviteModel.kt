package pw.vintr.music.domain.server.model

import pw.vintr.music.data.server.dto.ServerInviteDto
import pw.vintr.music.tools.format.DateFormat
import java.util.Date

data class ServerInviteModel(
    val id: String,
    val server: ServerModel,
    val code: Int,
    val isUnlimited: Boolean,
    val expiry: Date?
)

fun ServerInviteDto.toModel() = ServerInviteModel(
    id = id,
    server = server.toModel(haveAccessControl = true),
    code = code,
    isUnlimited = isUnlimited,
    expiry = DateFormat.parseDate(expiry)
)
