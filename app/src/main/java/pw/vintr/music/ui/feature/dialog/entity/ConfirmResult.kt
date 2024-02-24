package pw.vintr.music.ui.feature.dialog.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ConfirmResult : Parcelable {
    ACCEPT,
    DECLINE;

    companion object {
        const val KEY = "confirm-result"
    }
}
