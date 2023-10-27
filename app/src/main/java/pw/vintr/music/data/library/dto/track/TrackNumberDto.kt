package pw.vintr.music.data.library.dto.track

import com.google.gson.annotations.SerializedName

data class TrackNumberDto(
    @SerializedName("no")
    val no: Int,
    @SerializedName("of")
    val of: Int
)
