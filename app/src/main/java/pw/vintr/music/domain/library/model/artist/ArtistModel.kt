package pw.vintr.music.domain.library.model.artist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import pw.vintr.music.tools.http.MediaUrlBuilder

@Serializable
@Parcelize
data class ArtistModel(
    val name: String,
    val artworkUrl: String = MediaUrlBuilder.artworkForArtist(name)
) : Parcelable
