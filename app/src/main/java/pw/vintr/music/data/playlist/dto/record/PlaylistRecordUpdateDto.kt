package pw.vintr.music.data.playlist.dto.record

import com.google.gson.annotations.SerializedName

data class PlaylistRecordUpdateDto(
    @SerializedName("trackId")
    val trackId: String,
    @SerializedName("ordinal")
    val ordinal: Int,
)
