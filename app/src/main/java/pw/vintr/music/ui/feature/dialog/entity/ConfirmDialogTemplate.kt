package pw.vintr.music.ui.feature.dialog.entity

import pw.vintr.music.R
import pw.vintr.music.ui.navigation.Navigator
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen
import pw.vintr.music.ui.navigation.navResult.ResultListenerHandler

object ConfirmDialogTemplate {

    fun Navigator.openLogoutConfirmDialog(onLogout: () -> Unit): ResultListenerHandler {
        return forwardWithResult<ConfirmResult>(
            screen = Screen.ConfirmDialog(
                data = ConfirmDialogData.Resource(
                    titleRes = R.string.confirm_logout_title,
                    messageRes = R.string.confirm_logout_description,
                    acceptTextRes = R.string.logout,
                    declineTextRes = R.string.common_cancel
                )
            ),
            type = NavigatorType.Root,
            resultKey = ConfirmResult.KEY,
        ) {
            if (it == ConfirmResult.ACCEPT) { onLogout() }
        }
    }
}
