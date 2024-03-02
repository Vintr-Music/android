package pw.vintr.music.ui.feature.actionSheet.album.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import pw.vintr.music.R

@Parcelize
enum class AlbumAction(
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
    );
}
