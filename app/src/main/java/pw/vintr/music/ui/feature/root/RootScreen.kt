package pw.vintr.music.ui.feature.root

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.extension.getRequiredArg
import pw.vintr.music.ui.feature.albumDetails.AlbumDetailsScreen
import pw.vintr.music.ui.feature.artistDetails.ArtistDetailsScreen
import pw.vintr.music.ui.feature.equalizer.EqualizerScreen
import pw.vintr.music.ui.feature.home.HomeScreen
import pw.vintr.music.ui.feature.library.LibraryScreen
import pw.vintr.music.ui.feature.library.favorite.albumFavoriteList.AlbumFavoriteListScreen
import pw.vintr.music.ui.feature.library.favorite.artistFavoriteList.ArtistFavoriteListScreen
import pw.vintr.music.ui.feature.library.artist.ArtistListScreen
import pw.vintr.music.ui.feature.library.playlist.PlaylistListScreen
import pw.vintr.music.ui.feature.library.playlist.details.PlaylistDetailsScreen
import pw.vintr.music.ui.feature.menu.MenuScreen
import pw.vintr.music.ui.feature.search.SearchScreen
import pw.vintr.music.ui.feature.settings.SettingsScreen
import pw.vintr.music.ui.kit.layout.NowPlayingLayout
import pw.vintr.music.ui.kit.navbar.AppNavBarItem
import pw.vintr.music.ui.kit.navbar.AppNavigationBar
import pw.vintr.music.ui.kit.sliding.BottomSheetScaffoldState
import pw.vintr.music.ui.kit.sliding.rememberBottomSheetScaffoldState
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorAction
import pw.vintr.music.ui.navigation.NavigatorEffect
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen
import pw.vintr.music.ui.navigation.navArgs.parcelable.ParcelableNavType

private const val TRANSITION_DURATION = 300

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun RootScreen(
    viewModel: RootViewModel = koinViewModel()
) {
    StatusBarEffect()

    val navController = rememberNavController()
    val tabs = viewModel.bottomTabs.collectAsState()
    val playerState = viewModel.playerStateFlow.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Restore navigator type in case of "warm start"
    LaunchedEffect(key1 = true) {
        val navBackStackEntry = navController.currentBackStackEntry
        val currentDestination = navBackStackEntry?.destination

        currentDestination?.route?.let { viewModel.restoreNavigatorType(it) }
    }

    NowPlayingLayout(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        state = playerState.value,
        scaffoldState = scaffoldState,
        onControlClick = {
            viewModel.onNowPlayingControlClick(playerState.value)
        },
        onSeekToTrack = { index ->
            viewModel.seekToTrack(index)
        },
        content = { modifier ->
            NavHost(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                startDestination = Tab.Home.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                tabs.value.forEach { tab ->
                    composable(tab.route) {
                        TabNavigation(
                            modifier = Modifier.fillMaxSize(),
                            rootScreen = tab.rootScreen,
                            navigatorType = tab.navigatorType,
                            scaffoldState = scaffoldState,
                        )
                    }
                }
            }
        },
        bottomNavigation = { modifier ->
            AppNavigationBar(modifier = modifier) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                tabs.value.forEach { tab ->
                    val isSelected = currentDestination?.hierarchy
                        ?.any { it.route == tab.route } == true

                    AppNavBarItem(
                        selected = isSelected,
                        icon = tab.iconRes,
                        onClick = {
                            if (isSelected) {
                                viewModel.backToTabStart(tab.navigatorType)
                            } else {
                                navController.openTab(tab)
                                viewModel.setNavigatorType(tab.navigatorType)
                            }
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabNavigation(
    modifier: Modifier = Modifier,
    rootScreen: Screen,
    navigatorType: NavigatorType,
    navigator: Navigator = koinInject(),
    scaffoldState: BottomSheetScaffoldState,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()

    fun closeNowPlaying() {
        coroutineScope.launch { scaffoldState.bottomSheetState.collapse() }
    }

    NavigatorEffect(
        type = navigatorType,
        navigator = navigator,
        controller = navController,
        onCustomCommand = { action ->
            if (action is NavigatorAction.CloseNowPlaying) {
                closeNowPlaying()
            }
        }
    )

    // Root back handler
    BackHandler(enabled = navBackStackEntry?.destination?.route == rootScreen.route) {
        activity?.finish()
    }

    NavHost(
        modifier = modifier,
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
        composable(Screen.Equalizer.route) { EqualizerScreen() }
        composable(
            Screen.AlbumDetails.routeTemplate,
            arguments = listOf(
                navArgument(Screen.AlbumDetails.ARG_KEY_ALBUM) {
                    type = ParcelableNavType(AlbumModel::class.java)
                }
            )
        ) {entry ->
            val album = entry.arguments.getRequiredArg(
                Screen.AlbumDetails.ARG_KEY_ALBUM,
                AlbumModel::class.java
            )

            AlbumDetailsScreen(album = album)
        }
        composable(
            route = Screen.ArtistDetails.routeTemplate,
            arguments = listOf(
                navArgument(Screen.ArtistDetails.ARG_KEY_ARTIST) {
                    type = ParcelableNavType(ArtistModel::class.java)
                }
            )
        ) {entry ->
            val artist = entry.arguments.getRequiredArg(
                Screen.ArtistDetails.ARG_KEY_ARTIST,
                ArtistModel::class.java
            )

            ArtistDetailsScreen(artist = artist)
        }
        composable(
            route = Screen.PlaylistDetails.routeTemplate,
            arguments = listOf(
                navArgument(Screen.PlaylistDetails.ARG_KEY_PLAYLIST_ID) {
                    defaultValue = String.Empty
                }
            )
        ) {
            val playlistId = it.arguments
                ?.getString(Screen.PlaylistDetails.ARG_KEY_PLAYLIST_ID) ?: String.Empty

            PlaylistDetailsScreen(playlistId = playlistId)
        }
        composable(Screen.ArtistList.route) { ArtistListScreen() }
        composable(Screen.PlaylistList.route) { PlaylistListScreen() }
        composable(Screen.ArtistFavoriteList.route) { ArtistFavoriteListScreen() }
        composable(Screen.AlbumFavoriteList.route) { AlbumFavoriteListScreen() }
    }

    // Scaffold bottom sheet back handler
    BackHandler(enabled = scaffoldState.bottomSheetState.isExpanded) { closeNowPlaying() }
}

private fun NavController.openTab(tab: Tab) {
    navigate(tab.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }

        launchSingleTop = true
        restoreState = true
    }
}
