package pw.vintr.music.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.rememberKoinInject
import pw.vintr.music.ui.feature.register.RegisterScreen
import pw.vintr.music.ui.feature.serverSelection.ServerSelectionScreen
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorEffect
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

private const val TRANSITION_DURATION = 300

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator(skipHalfExpanded = true)
            val navController = rememberNavController(bottomSheetNavigator)

            VintrMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        navController = navController,
                        rootScreen = Screen.Login
                    )
                }
            }
        }
    }
}

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
        composable(Screen.Login.route) { LoginScreen() }
        composable(Screen.Register.route) { RegisterScreen() }
        composable(Screen.SelectServer.route) { ServerSelectionScreen() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterialNavigationApi
@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
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
