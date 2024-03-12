package pw.vintr.music.ui.feature.actionSheet.track.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackActionResult(
    val action: TrackAction
) : Parcelable {

    companion object {
        const val KEY = "track-action-result"
    }
}
