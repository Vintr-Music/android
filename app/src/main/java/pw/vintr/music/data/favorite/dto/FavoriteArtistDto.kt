package pw.vintr.music.data.favorite.dto

import com.google.gson.annotations.SerializedName

data class FavoriteArtistDto(
    @SerializedName("artist")
    val artist: String,
)
