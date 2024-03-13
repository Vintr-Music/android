package pw.vintr.music.domain.playlist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.playlist.dto.PlaylistDto
import pw.vintr.music.tools.http.MediaUrlBuilder
import java.util.Calendar

@Parcelize
data class PlaylistModel(
    val id: String,
    val owner: String,
    val name: String,
    val description: String,
    val createdAt: Calendar,
    val artworkUrl: String = MediaUrlBuilder.artworkForPlaylist(id)
) : Parcelable

fun PlaylistDto.toModel() = PlaylistModel(
    id = id,
    owner = owner,
    name = name,
    description = description,
    createdAt = Calendar.getInstance().apply {
        set(Calendar.MILLISECOND, createdAt.toInt())
    }
)
