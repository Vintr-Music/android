package pw.vintr.music.domain.library.model.track

import pw.vintr.music.data.library.dto.track.TrackFormatDto
import pw.vintr.music.tools.format.DurationFormat

data class TrackFormatModel(
    val bitrate: Double,
    val codec: String,
    val codecProfile: String,
    val container: String,
    val duration: Double,
    val lossless: Boolean,
    val numberOfChannels: Int,
    val sampleRate: Int,
    val tagTypes: List<String>,
    val tool: String
) {
    val durationFormat = DurationFormat.formatSeconds(duration.toLong())
}

fun TrackFormatDto.toModel() = TrackFormatModel(
    bitrate,
    codec.orEmpty(),
    codecProfile.orEmpty(),
    container.orEmpty(),
    duration,
    lossless,
    numberOfChannels,
    sampleRate,
    tagTypes.orEmpty(),
    tool.orEmpty(),
)
