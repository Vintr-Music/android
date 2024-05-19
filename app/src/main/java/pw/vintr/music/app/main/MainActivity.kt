package pw.vintr.music.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext
import org.koin.compose.rememberKoinInject
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.extension.getRequiredArg
import pw.vintr.music.tools.extension.getRequiredArgList
import pw.vintr.music.tools.extension.noRippleClickable
import pw.vintr.music.ui.feature.actionSheet.album.AlbumActionSheet
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.playlist.PlaylistActionSheet
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionSheetInfo
import pw.vintr.music.ui.feature.dialog.ConfirmDialog
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogData
import pw.vintr.music.ui.feature.nowPlaying.manageSession.ManageSessionScreen
import pw.vintr.music.ui.feature.register.RegisterScreen
import pw.vintr.music.ui.feature.root.RootScreen
import pw.vintr.music.ui.feature.server.accessControl.ServerAccessControlScreen
import pw.vintr.music.ui.feature.server.accessControl.invite.details.ServerInviteDetailsBottomSheet
import pw.vintr.music.ui.feature.server.selection.ServerSelectionScreen
import pw.vintr.music.ui.feature.server.selection.connectNew.ConnectNewServerScreen
import pw.vintr.music.ui.feature.actionSheet.track.TrackActionSheet
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.feature.library.playlist.addTrack.PlaylistAddTrackScreen
import pw.vintr.music.ui.feature.library.playlist.create.PlaylistCreateScreen
import pw.vintr.music.ui.feature.library.playlist.edit.PlaylistEditScreen
import pw.vintr.music.ui.kit.alert.Alert
import pw.vintr.music.ui.kit.sliding.AnchoredDraggableDefaults
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorEffect
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen
import pw.vintr.music.ui.navigation.navArgs.parcelable.ParcelableNavType
import pw.vintr.music.ui.navigation.navArgs.parcelableList.ParcelableListNavType
import pw.vintr.music.ui.navigation.navGraph.extendedDialog
import pw.vintr.music.ui.theme.Bee1

private const val TRANSITION_DURATION = 300

private const val ALERT_SHOW_DURATION = 2500L

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
                            PrimaryLoader()
                            AlertHolder()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun PrimaryLoader() {
        val primaryLoaderState = viewModel.primaryLoaderState.collectAsState()

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = primaryLoaderState.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                    .noRippleClickable(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = Bee1,
                )
            }
        }
    }

    @Composable
    private fun AlertHolder() {
        val alertState = viewModel.alertState.collectAsState()

        LaunchedEffect(key1 = alertState.value) {
            if (alertState.value.alertVisible) {
                delay(ALERT_SHOW_DURATION)
                viewModel.hideAlert()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            AnimatedVisibility(
                visible = alertState.value.alert != null && alertState.value.show,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit =  shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Alert(
                    title = alertState.value.alert?.titleRes
                        ?.let { stringResource(id = it) }
                        .orEmpty(),
                    message = alertState.value.alert?.messageRes
                        ?.let { stringResource(id = it) }
                        .orEmpty(),
                    onCloseAction = { viewModel.hideAlert() }
                )
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
        bottomSheet(
            Screen.ServerInviteDetails.routeTemplate,
            arguments = listOf(
                navArgument(Screen.ServerInviteDetails.ARG_KEY_SERVER_INVITE) {
                    type = ParcelableNavType(ServerInviteModel::class.java)
                }
            )
        ) {
            BackHandler { navController.navigateUp() }

            val invite = it.arguments.getRequiredArg<ServerInviteModel>(
                Screen.ServerInviteDetails.ARG_KEY_SERVER_INVITE,
            )
            ServerInviteDetailsBottomSheet(invite = invite)
        }

        // Root, bottom sheets, dialogs
        composable(Screen.Root.route) { RootScreen() }
        bottomSheet(
            Screen.TrackActionSheet.routeTemplate,
            arguments = listOf(
                navArgument(Screen.TrackActionSheet.ARG_KEY_TRACK) {
                    type = ParcelableNavType(TrackModel::class.java)
                },
                navArgument(Screen.TrackActionSheet.ARG_KEY_ACTIONS) {
                    type = ParcelableListNavType<TrackAction>()
                }
            )
        ) {
            BackHandler { navController.navigateUp() }

            val trackModel = it.arguments.getRequiredArg<TrackModel>(
                Screen.TrackActionSheet.ARG_KEY_TRACK
            )
            val allowedOptions = it.arguments.getRequiredArgList<TrackAction>(
                Screen.TrackActionSheet.ARG_KEY_ACTIONS
            )
            TrackActionSheet(
                trackModel = trackModel,
                allowedActions = allowedOptions
            )
        }

        bottomSheet(
            Screen.AlbumActionSheet.routeTemplate,
            arguments = listOf(
                navArgument(Screen.AlbumActionSheet.ARG_KEY_SHEET_INFO) {
                    type = ParcelableNavType(AlbumActionSheetInfo::class.java)
                },
            )
        ) {
            BackHandler { navController.navigateUp() }

            val actionSheetInfo = it.arguments.getRequiredArg<AlbumActionSheetInfo>(
                Screen.AlbumActionSheet.ARG_KEY_SHEET_INFO
            )
            AlbumActionSheet(actionSheetInfo)
        }

        bottomSheet(
            Screen.PlaylistActionSheet.routeTemplate,
            arguments = listOf(
                navArgument(Screen.PlaylistActionSheet.ARG_KEY_SHEET_INFO) {
                    type = ParcelableNavType(PlaylistActionSheetInfo::class.java)
                },
            )
        ) {
            BackHandler { navController.navigateUp() }

            val actionSheetInfo = it.arguments.getRequiredArg<PlaylistActionSheetInfo>(
                Screen.PlaylistActionSheet.ARG_KEY_SHEET_INFO
            )
            PlaylistActionSheet(actionSheetInfo)
        }

        extendedDialog(
            route = Screen.ConfirmDialog.routeTemplate,
            arguments = listOf(
                navArgument(Screen.ConfirmDialog.ARG_KEY_DATA) {
                    type = ParcelableNavType(ConfirmDialogData::class.java)
                },
            )
        ) {
            val dialogData = it.arguments.getRequiredArg<ConfirmDialogData>(
                Screen.ConfirmDialog.ARG_KEY_DATA
            )
            ConfirmDialog(data = dialogData)
        }

        // Session management
        bottomSheet(Screen.ManageSession.route) {
            BackHandler { navController.navigateUp() }
            ManageSessionScreen()
        }

        // Playlist actions
        bottomSheet(Screen.PlaylistCreate.route) {
            BackHandler { navController.navigateUp() }
            PlaylistCreateScreen()
        }

        bottomSheet(
            Screen.PlaylistAddTrack.routeTemplate,
            arguments = listOf(
                navArgument(Screen.PlaylistAddTrack.ARG_KEY_TRACK_ID) {
                    defaultValue = String.Empty
                }
            )
        ) {
            BackHandler { navController.navigateUp() }
            val trackId = it.arguments
                ?.getString(Screen.PlaylistAddTrack.ARG_KEY_TRACK_ID) ?: String.Empty

            PlaylistAddTrackScreen(trackId)
        }

        bottomSheet(
            Screen.PlaylistEdit.routeTemplate,
            arguments = listOf(
                navArgument(Screen.PlaylistEdit.ARG_KEY_PLAYLIST_ID) {
                    defaultValue = String.Empty
                }
            )
        ) {
            BackHandler { navController.navigateUp() }
            val playlistId = it.arguments
                ?.getString(Screen.PlaylistEdit.ARG_KEY_PLAYLIST_ID) ?: String.Empty

            PlaylistEditScreen(playlistId)
        }
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
