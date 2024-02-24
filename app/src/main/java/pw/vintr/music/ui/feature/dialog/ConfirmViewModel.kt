package pw.vintr.music.ui.feature.dialog

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.dialog.entity.ConfirmResult
import pw.vintr.music.ui.navigation.NavigatorType

class ConfirmViewModel : BaseViewModel() {

    fun accept() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = ConfirmResult.KEY,
            result = ConfirmResult.ACCEPT
        )
    }

    fun decline() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = ConfirmResult.KEY,
            result = ConfirmResult.DECLINE
        )
    }
}
