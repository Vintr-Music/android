package pw.vintr.music.ui.feature.actionSheet.track.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import pw.vintr.music.R

@Parcelize
enum class TrackAction(
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
    ),
    PLAY_NEXT(
        iconRes = R.drawable.ic_play_next,
        titleRes = R.string.play_next
    ),
    ADD_TO_QUEUE(
        iconRes = R.drawable.ic_add_to_queue,
        titleRes = R.string.add_to_queue
    ),
    ADD_TO_PLAYLIST(
        iconRes = R.drawable.ic_add_to_playlist,
        titleRes = R.string.playlist_add
    );

    companion object {
        val actionsExceptAlbumNavigate = listOf(
            GO_TO_ARTIST,
            PLAY_NEXT,
            ADD_TO_QUEUE,
            ADD_TO_PLAYLIST
        )
        val actionsExceptArtistNavigate = listOf(
            GO_TO_ALBUM,
            PLAY_NEXT,
            ADD_TO_QUEUE,
            ADD_TO_PLAYLIST
        )
    }
}
