package pw.vintr.music.data.library.dto.track

import com.google.gson.annotations.SerializedName

data class TrackFormatDto(
    @SerializedName("bitrate")
    val bitrate: Int,
    @SerializedName("codec")
    val codec: String,
    @SerializedName("codecProfile")
    val codecProfile: String,
    @SerializedName("container")
    val container: String,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("lossless")
    val lossless: Boolean,
    @SerializedName("numberOfChannels")
    val numberOfChannels: Int,
    @SerializedName("sampleRate")
    val sampleRate: Int,
    @SerializedName("tagTypes")
    val tagTypes: List<String>,
    @SerializedName("tool")
    val tool: String
)
