package pw.vintr.music.domain.library.model.artist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.BuildConfig

@Parcelize
data class ArtistModel(
    val name: String,
    val artworkUrl: String = BuildConfig.BASE_URL + "api/library/artworks/artist?artist=$name"
) : Parcelable
