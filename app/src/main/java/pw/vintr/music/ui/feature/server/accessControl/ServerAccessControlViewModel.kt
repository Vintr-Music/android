package pw.vintr.music.ui.feature.server.accessControl

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pw.vintr.music.R
import pw.vintr.music.ui.base.BaseViewModel

class ServerAccessControlViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(value = ServerAccessControlScreenState())

    val screenState = _screenState.asStateFlow()

    fun selectTab(tab: ServerAccessControlTab) {
        _screenState.update { it.copy(selectedTab = tab) }
    }
}

enum class ServerAccessControlTab(@StringRes val titleRes: Int) {
    INVITES(titleRes = R.string.invites),
    MEMBERS(titleRes = R.string.members)
}

data class ServerAccessControlScreenState(
    val tabs: List<ServerAccessControlTab> = listOf(
        ServerAccessControlTab.INVITES,
        ServerAccessControlTab.MEMBERS
    ),
    val selectedTab: ServerAccessControlTab = ServerAccessControlTab.INVITES
) {
    val selectedIndex = tabs.indexOf(selectedTab)
}
