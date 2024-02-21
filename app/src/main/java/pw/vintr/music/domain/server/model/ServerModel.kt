package pw.vintr.music.domain.server.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.server.dto.ServerDto

@Parcelize
data class ServerModel(
    val id: String,
    val name: String,
    val owner: String,
    val users: List<String>,
    val haveAccessControl: Boolean
) : Parcelable

fun ServerDto.toModel(
    haveAccessControl: Boolean = false
) = ServerModel(id, name, owner, users, haveAccessControl)
