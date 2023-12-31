package pw.vintr.music.ui.feature.menu.logout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LogoutConfirmResult : Parcelable {
    LOGOUT;

    companion object {
        const val KEY = "logout-confirm-result"
    }
}
