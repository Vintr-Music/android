package pw.vintr.music.ui.feature.trackDetails.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import pw.vintr.music.R

@Parcelize
enum class TrackDetailsOption(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val titleRes: Int
) : Parcelable {
    GO_TO_ALBUM(
        iconRes = R.drawable.ic_album,
        titleRes = R.string.open_album
    ),
    GO_TO_ARTIST(
        iconRes = R.drawable.ic_artist,
        titleRes = R.string.open_artist
    );

    companion object {
        val optionsExceptAlbumNavigate = listOf(
            GO_TO_ARTIST
        )
        val optionsExceptArtistNavigate = listOf(
            GO_TO_ALBUM
        )
    }
}
