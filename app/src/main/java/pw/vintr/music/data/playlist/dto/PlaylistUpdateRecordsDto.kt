package pw.vintr.music.data.playlist.dto

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.playlist.dto.record.PlaylistRecordUpdateDto

data class PlaylistUpdateRecordsDto(
    @SerializedName("playlistId")
    val playlistId: String,
    @SerializedName("updateData")
    val updateData: List<PlaylistRecordUpdateDto>,
)
