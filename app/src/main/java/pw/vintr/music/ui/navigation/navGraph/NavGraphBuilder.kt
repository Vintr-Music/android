package pw.vintr.music.ui.navigation.navGraph

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.get
import pw.vintr.music.ui.kit.dialog.DialogContainer

fun NavGraphBuilder.extendedDialog(
    route: String,
    controller: NavController,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        DialogNavigator.Destination(
            provider[DialogNavigator::class],
            dialogProperties,
        ) {
            // Remove android default dim
            (LocalView.current.parent as? DialogWindowProvider)?.window?.apply {
                setDimAmount(0f)
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }

            // Wrap dialog in custom container
            DialogContainer(
                onDimClick = {
                    controller.navigateUp()
                }
            ) { content(it) }

        }.apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}
