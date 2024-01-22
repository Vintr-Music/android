package pw.vintr.music.data.pagination

import com.google.gson.annotations.SerializedName

data class PageDto<T>(
    @SerializedName("data")
    val data: List<T>,
    @SerializedName("count")
    val count: Int
)
