package pw.vintr.music.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import pw.vintr.music.ui.feature.login.LoginScreen
import pw.vintr.music.ui.theme.VintrMusicTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext
import org.koin.compose.rememberKoinInject
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.extension.getRequiredArg
import pw.vintr.music.ui.feature.menu.logout.LogoutConfirmDialog
import pw.vintr.music.ui.feature.register.RegisterScreen
import pw.vintr.music.ui.feature.root.RootScreen
import pw.vintr.music.ui.feature.server.accessControl.ServerAccessControlScreen
import pw.vintr.music.ui.feature.server.selection.ServerSelectionScreen
import pw.vintr.music.ui.feature.server.selection.connectNew.ConnectNewServerScreen
import pw.vintr.music.ui.feature.trackDetails.TrackDetailsBottomSheet
import pw.vintr.music.ui.kit.sliding.AnchoredDraggableDefaults
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorEffect
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen
import pw.vintr.music.ui.navigation.navArgs.parcelable.ParcelableNavType
import pw.vintr.music.ui.navigation.navGraph.extendedDialog

private const val TRANSITION_DURATION = 300

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val initialState = viewModel.initialState.collectAsState()
            val bottomSheetNavigator = rememberBottomSheetNavigator(skipHalfExpanded = true)
            val navController = rememberNavController(bottomSheetNavigator)

            val audioRecordPermissionState = rememberPermissionState(
                permission = android.Manifest.permission.RECORD_AUDIO
            )

            LaunchedEffect(Unit) {
                audioRecordPermissionState.launchPermissionRequest()
            }
            LaunchedEffect(audioRecordPermissionState.status.isGranted) {
                viewModel.setAudioPermissionGranted(audioRecordPermissionState.status.isGranted)
            }

            CompositionLocalProvider(LocalActivity provides this) {
                KoinContext {
                    VintrMusicTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ModalBottomSheetLayout(
                                bottomSheetNavigator = bottomSheetNavigator,
                                sheetContentColor = Color.Transparent,
                                sheetBackgroundColor = Color.Transparent,
                                scrimColor = MaterialTheme.colorScheme.background
                                    .copy(alpha = 0.9f),
                                sheetElevation = 0.dp
                            ) {
                                Navigation(
                                    navController = navController,
                                    rootScreen = when (initialState.value) {
                                        AppInitialState.Authorized -> Screen.Root
                                        AppInitialState.Login -> Screen.Login
                                        AppInitialState.ServerSelection -> Screen.SelectServer()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun Navigation(
    navigator: Navigator = rememberKoinInject(),
    navController: NavHostController,
    rootScreen: Screen
) {
    NavigatorEffect(
        type = NavigatorType.Root,
        navigator = navigator,
        controller = navController,
    )

    NavHost(
        navController = navController,
        startDestination = rootScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(TRANSITION_DURATION)) },
        exitTransition = { fadeOut(animationSpec = tween(TRANSITION_DURATION)) }
    ) {
        // Login flow
        composable(Screen.Login.route) { LoginScreen() }
        composable(Screen.Register.route) { RegisterScreen() }
        composable(
            Screen.SelectServer.routeTemplate,
            arguments = listOf(
                navArgument(Screen.SelectServer.ARG_IS_INITIALIZE_MODE) { defaultValue = true }
            )
        ) {
            val isInitializeMode = it.arguments
                ?.getBoolean(Screen.SelectServer.ARG_IS_INITIALIZE_MODE) ?: true

            ServerSelectionScreen(isInitializeMode = isInitializeMode)
        }

        // Server flow
        composable(Screen.ConnectNewServer.route) { ConnectNewServerScreen() }
        composable(
            Screen.ServerAccessControl.routeTemplate,
            arguments = listOf(
                navArgument(Screen.ServerAccessControl.ARG_KEY_SERVER_ID) {
                    defaultValue = String.Empty
                }
            )
        ) {
            val serverId = it.arguments
                ?.getString(Screen.ServerAccessControl.ARG_KEY_SERVER_ID) ?: String.Empty

            ServerAccessControlScreen(serverId = serverId)
        }

        // Root, bottom sheets, dialogs
        composable(Screen.Root.route) { RootScreen() }
        bottomSheet(
            Screen.TrackDetails.routeTemplate,
            arguments = listOf(
                navArgument(Screen.TrackDetails.ARG_KEY_TRACK) {
                    type = ParcelableNavType(TrackModel::class.java)
                }
            )
        ) {
            BackHandler { navController.navigateUp() }

            val trackModel = it.arguments.getRequiredArg(
                Screen.TrackDetails.ARG_KEY_TRACK,
                TrackModel::class.java
            )
            TrackDetailsBottomSheet(trackModel = trackModel)
        }
        extendedDialog(
            route = Screen.LogoutConfirmDialog.route,
            controller = navController
        ) { LogoutConfirmDialog() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterialNavigationApi
@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    skipHalfExpanded: Boolean = false,
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec,
        skipHalfExpanded = skipHalfExpanded
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}
