package pw.vintr.music.data.library.dto.track

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("format")
    val format: TrackFormatDto,
    @SerializedName("md5")
    val md5: String,
    @SerializedName("metadata")
    val metadata: TrackMetadataDto
)
