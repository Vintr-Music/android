package pw.vintr.music.domain.server.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import pw.vintr.music.data.server.dto.ServerInviteDto
import pw.vintr.music.tools.format.DateFormat
import pw.vintr.music.tools.serialization.DateSerializer
import java.util.Date

@Serializable
@Parcelize
data class ServerInviteModel(
    val id: String,
    val server: ServerModel,
    val code: Int,
    val isUnlimited: Boolean,
    @Serializable(with = DateSerializer::class)
    val expiry: Date?
) : Parcelable

fun ServerInviteDto.toModel() = ServerInviteModel(
    id = id,
    server = server.toModel(haveAccessControl = true),
    code = code,
    isUnlimited = isUnlimited,
    expiry = DateFormat.parseDate(expiry)
)
