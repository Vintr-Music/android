package pw.vintr.music.data.library.dto.search

import com.google.gson.annotations.SerializedName
import pw.vintr.music.data.library.dto.album.AlbumDto
import pw.vintr.music.data.library.dto.track.TrackDto

data class SearchContentDto(
    @SerializedName("artists")
    val artists: List<String>,
    @SerializedName("albums")
    val albums: List<AlbumDto>,
    @SerializedName("tracks")
    val tracks: List<TrackDto>,
)
