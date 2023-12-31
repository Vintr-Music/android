package pw.vintr.music.ui.feature.menu.logout

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType

class LogoutConfirmViewModel : BaseViewModel() {

    fun logout() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = LogoutConfirmResult.KEY,
            result = LogoutConfirmResult.LOGOUT
        )
    }
}
