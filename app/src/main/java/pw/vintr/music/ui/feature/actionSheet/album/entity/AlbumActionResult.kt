package pw.vintr.music.ui.feature.actionSheet.album.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumActionResult(
    val action: AlbumAction
) : Parcelable {

    companion object {
        const val KEY = "album-action-result"
    }
}
