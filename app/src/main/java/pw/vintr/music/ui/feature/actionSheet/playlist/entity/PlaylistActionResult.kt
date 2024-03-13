package pw.vintr.music.ui.feature.actionSheet.playlist.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistActionResult(
    val action: PlaylistAction
) : Parcelable {

    companion object {
        const val KEY = "playlist-action-result"
    }
}
