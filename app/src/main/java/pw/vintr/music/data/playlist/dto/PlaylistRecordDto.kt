package pw.vintr.music.data.playlist.dto

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.library.dto.track.TrackDto

data class PlaylistRecordDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("playlistId")
    val playlistId: String,
    @SerializedName("ordinal")
    val ordinal: Int,
    @SerializedName("track")
    val track: TrackDto
)
