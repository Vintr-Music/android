package pw.vintr.music.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen(val route: String) : Parcelable {

    object Login : Screen(route = "login")

    object Register : Screen(route = "register")
}
