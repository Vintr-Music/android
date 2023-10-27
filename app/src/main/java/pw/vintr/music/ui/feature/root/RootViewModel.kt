package pw.vintr.music.ui.feature.root

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.R
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

sealed interface TabNavigator : NavigatorType {
    object Home : TabNavigator
    object Search : TabNavigator
    object Library : TabNavigator
    object Menu : TabNavigator
}

sealed class Tab(
    val route: String,
    @DrawableRes
    val iconRes: Int,
    val navigatorType: NavigatorType,
    val rootScreen: Screen,
) {
    object Home : Tab(
        route = "home",
        iconRes = R.drawable.ic_home,
        navigatorType = TabNavigator.Home,
        rootScreen = Screen.Home
    )
    object Search : Tab(
        route = "search",
        iconRes = R.drawable.ic_search,
        navigatorType = TabNavigator.Search,
        rootScreen = Screen.Search
    )
    object Library : Tab(
        route = "library",
        iconRes = R.drawable.ic_library,
        navigatorType = TabNavigator.Library,
        rootScreen = Screen.Library
    )
    object Menu : Tab(
        route = "menu",
        iconRes = R.drawable.ic_profile,
        navigatorType = TabNavigator.Menu,
        rootScreen = Screen.Menu
    )
}

val tabs = listOf(
    Tab.Home,
    Tab.Search,
    Tab.Library,
    Tab.Menu
)

class RootViewModel : BaseViewModel() {

    init {
        setNavigatorType(TabNavigator.Home)
    }

    val bottomTabs = MutableStateFlow(tabs).asStateFlow()

    fun setNavigatorType(navigatorType: NavigatorType) {
        navigator.switchNavigatorType(navigatorType)
    }
}
