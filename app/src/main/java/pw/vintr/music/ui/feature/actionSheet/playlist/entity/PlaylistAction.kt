package pw.vintr.music.ui.feature.actionSheet.playlist.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import pw.vintr.music.R

@Parcelize
enum class PlaylistAction(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val titleRes: Int
) : Parcelable {
    PLAY_NEXT(
        iconRes = R.drawable.ic_play_next,
        titleRes = R.string.play_next
    ),
    ADD_TO_QUEUE(
        iconRes = R.drawable.ic_add_to_queue,
        titleRes = R.string.add_to_queue
    ),
    EDIT_PLAYLIST(
        iconRes = R.drawable.ic_edit,
        titleRes = R.string.playlist_edit
    ),
    DELETE_PLAYLIST(
        iconRes = R.drawable.ic_delete,
        titleRes = R.string.playlist_delete
    );
}
