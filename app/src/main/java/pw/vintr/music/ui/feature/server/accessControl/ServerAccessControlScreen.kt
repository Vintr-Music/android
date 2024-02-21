package pw.vintr.music.ui.feature.server.accessControl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.feature.server.accessControl.invite.ServerInviteListTab
import pw.vintr.music.ui.feature.server.accessControl.members.ServerMemberListTab
import pw.vintr.music.ui.kit.selector.SegmentControl
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun ServerAccessControlScreen(
    serverId: String,
    viewModel: ServerAccessControlViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.access_control),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(scaffoldPadding)
        ) {
            val screenState = viewModel.screenState.collectAsState()

            SegmentControl(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    )
                    .height(36.dp),
                items = screenState.value.tabs.map {
                    stringResource(id = it.titleRes)
                },
                selectedItemIndex = screenState.value.selectedIndex,
                onSelectedTab = { viewModel.selectTab(screenState.value.tabs[it]) }
            )
            when (screenState.value.selectedTab) {
                ServerAccessControlTab.INVITES -> {
                    ServerInviteListTab(serverId)
                }
                ServerAccessControlTab.MEMBERS -> {
                    ServerMemberListTab(serverId)
                }
            }
        }
    }
}
