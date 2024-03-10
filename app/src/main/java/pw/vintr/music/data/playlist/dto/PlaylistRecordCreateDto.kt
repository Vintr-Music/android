package pw.vintr.music.data.playlist.dto

import com.google.gson.annotations.SerializedName

data class PlaylistRecordCreateDto(
    @SerializedName("playlistId")
    val playlistId: String,
    @SerializedName("trackId")
    val trackId: String
)
