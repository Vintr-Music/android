package pw.vintr.music.data.library.dto.track

import com.google.gson.annotations.SerializedName

data class TrackMetadataDto(
    @SerializedName("album")
    val album: String,
    @SerializedName("artist")
    val artist: String,
    @SerializedName("artists")
    val artists: List<String>,
    @SerializedName("genre")
    val genre: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("track")
    val track: TrackNumberDto,
    @SerializedName("year")
    val year: Int
)
