package pw.vintr.music.tools.extension

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(
        navController.currentBackStack.value
            .firstOrNull { it.destination.route != null }
            ?.destination?.route ?: return
    ) {
        inclusive =  true
    }
}
