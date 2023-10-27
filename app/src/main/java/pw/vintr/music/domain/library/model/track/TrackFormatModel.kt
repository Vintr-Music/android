package pw.vintr.music.domain.library.model.track

import pw.vintr.music.data.library.dto.track.TrackFormatDto

data class TrackFormatModel(
    val bitrate: Int,
    val codec: String,
    val codecProfile: String,
    val container: String,
    val duration: Double,
    val lossless: Boolean,
    val numberOfChannels: Int,
    val sampleRate: Int,
    val tagTypes: List<String>,
    val tool: String
)

fun TrackFormatDto.toModel() = TrackFormatModel(
    bitrate,
    codec,
    codecProfile,
    container,
    duration,
    lossless,
    numberOfChannels,
    sampleRate,
    tagTypes,
    tool
)
