package pw.vintr.music.data.favorite.dto

import com.google.gson.annotations.SerializedName

data class FavoriteStatusDto(
    @SerializedName("status")
    val status: Boolean
)
