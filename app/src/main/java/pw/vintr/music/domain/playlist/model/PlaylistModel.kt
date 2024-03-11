package pw.vintr.music.domain.playlist.model

import pw.vintr.music.data.playlist.dto.PlaylistDto
import java.util.Calendar

data class PlaylistModel(
    val id: String,
    val owner: String,
    val name: String,
    val description: String,
    val createdAt: Calendar,
)

fun PlaylistDto.toModel() = PlaylistModel(
    id = id,
    owner = owner,
    name = name,
    description = description,
    createdAt = Calendar.getInstance().apply {
        set(Calendar.MILLISECOND, createdAt.toInt())
    }
)
