package pw.vintr.music.core.navigation.navGraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.get
import pw.vintr.music.core.ui.kit.modifier.dialogContainer

fun NavGraphBuilder.extendedDialog(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
    ),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        DialogNavigator.Destination(
            provider[DialogNavigator::class],
            dialogProperties,
        ) {
            ApplyDialogWindowDim()

            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .dialogContainer(),
                contentAlignment = Alignment.Center
            ) {
                content(it)
            }
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

@Composable
internal expect fun ApplyDialogWindowDim()
