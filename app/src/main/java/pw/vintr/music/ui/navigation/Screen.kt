package pw.vintr.music.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen(val route: String) : Parcelable {

    object Login : Screen(route = "login")

    object Register : Screen(route = "register")

    object SelectServer : Screen(route = "select-server")

    object Root : Screen(route = "root")

    object Home : Screen(route = "home")

    object Menu : Screen(route = "menu")

    object Settings : Screen(route = "settings")
}
