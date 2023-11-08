package pw.vintr.music.domain.library.model.track

import pw.vintr.music.BuildConfig
import pw.vintr.music.data.library.dto.track.TrackDto

data class TrackModel(
    val filePath: String,
    val format: TrackFormatModel,
    val md5: String,
    val metadata: TrackMetadataModel,
    val playerUrl: String,
)

fun TrackDto.toModel() = TrackModel(
    filePath = filePath,
    format = format.toModel(),
    md5 = md5,
    metadata = metadata.toModel(),
    playerUrl = BuildConfig.BASE_URL + "api/player/?key=$filePath"
)
