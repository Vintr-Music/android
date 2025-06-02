package pw.vintr.music.ui.feature.server.accessControl.invite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.server.invite.ServerInviteCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServerInviteListTab(
    serverId: String,
    viewModel: ServerInviteListViewModel = koinViewModel { parametersOf(serverId) }
) {
    val screenState = viewModel.screenState.collectAsState()

    ScreenStateLayout(
        state = screenState.value,
        errorRetryAction = { viewModel.loadData() }
    ) { state ->
        val inviteList = state.data
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { viewModel.refreshData() }
        )

        PullRefreshLayout(
            state = pullRefreshState,
            refreshing = state.isRefreshing
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = inviteList,
                    key = { it.id }
                ) { invite ->
                    ServerInviteCard(
                        invite = invite,
                        onClick = { viewModel.openInviteDetails(invite) }
                    )
                }
            }
        }
    }
}
