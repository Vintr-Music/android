package pw.vintr.music.ui.routing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen(val route: String) : Parcelable {

    object Login : Screen(route = "login")
}
