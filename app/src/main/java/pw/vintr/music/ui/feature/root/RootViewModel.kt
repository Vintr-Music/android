package pw.vintr.music.ui.feature.root

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.R
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType

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
) {
    object Home : Tab(
        route = "home",
        iconRes = R.drawable.ic_home,
        navigatorType = TabNavigator.Home,
    )
    object Search : Tab(
        route = "search",
        iconRes = R.drawable.ic_search,
        navigatorType = TabNavigator.Search,
    )
    object Library : Tab(
        route = "library",
        iconRes = R.drawable.ic_library,
        navigatorType = TabNavigator.Library,
    )
    object Menu : Tab(
        route = "menu",
        iconRes = R.drawable.ic_profile,
        navigatorType = TabNavigator.Menu,
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
