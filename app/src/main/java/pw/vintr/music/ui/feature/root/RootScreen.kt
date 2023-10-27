package pw.vintr.music.ui.feature.root

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import org.koin.compose.rememberKoinInject
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.feature.home.HomeScreen
import pw.vintr.music.ui.feature.library.LibraryScreen
import pw.vintr.music.ui.feature.menu.MenuScreen
import pw.vintr.music.ui.feature.search.SearchScreen
import pw.vintr.music.ui.feature.settings.SettingsScreen
import pw.vintr.music.ui.kit.navbar.AppNavBarItem
import pw.vintr.music.ui.kit.navbar.AppNavigationBar
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorEffect
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

private const val TRANSITION_DURATION = 300

@Composable
fun RootScreen(
    viewModel: RootViewModel = getViewModel()
) {
    StatusBarEffect()

    val navController = rememberNavController()
    val tabs = viewModel.bottomTabs.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = Tab.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(TRANSITION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(TRANSITION_DURATION)) }
        ) {
            tabs.value.forEach { tab ->
                composable(tab.route) {
                    TabNavigation(
                        rootScreen = tab.rootScreen,
                        navigatorType = tab.navigatorType
                    )
                }
            }
        }
        AppNavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            tabs.value.forEach { tab ->
                AppNavBarItem(
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == tab.route } == true,
                    icon = tab.iconRes,
                    onClick = {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        viewModel.setNavigatorType(tab.navigatorType)
                    }
                )
            }
        }
    }
}

@Composable
fun TabNavigation(
    rootScreen: Screen,
    navigatorType: NavigatorType,
    navigator: Navigator = rememberKoinInject(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val activity = (LocalContext.current as? Activity)

    NavigatorEffect(
        type = navigatorType,
        navigator = navigator,
        controller = navController
    )

    BackHandler(enabled = navBackStackEntry?.destination?.route == rootScreen.route) {
        activity?.finish()
    }

    NavHost(
        navController = navController,
        startDestination = rootScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(TRANSITION_DURATION)) },
        exitTransition = { fadeOut(animationSpec = tween(TRANSITION_DURATION)) }
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Library.route) { LibraryScreen() }
        composable(Screen.Menu.route) { MenuScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
