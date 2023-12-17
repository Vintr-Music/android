package pw.vintr.music.ui.feature.serverSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.button.ButtonText
import pw.vintr.music.ui.kit.server.ServerSelectableItem
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.toolbar.ToolbarPrimaryMount
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.theme.Gilroy32

@Composable
fun ServerSelectionScreen(
    usePrimaryMountToolbar: Boolean = true,
    viewModel: ServerSelectionViewModel = getViewModel()
) {
    val statusBarHeight = with (LocalDensity.current) {
        WindowInsets.statusBars.getTop(density = this).toDp()
    }
    val barHeight = statusBarHeight + 120.dp
    val screenState = viewModel.screenState.collectAsState()

    if (usePrimaryMountToolbar) {
        StatusBarEffect(useDarkIcons = true)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        if (usePrimaryMountToolbar) {
            SelectServerBar(modifier = Modifier.height(barHeight))
        } else {
            ToolbarRegular(
                title = stringResource(id = R.string.servers),
                onBackPressed = { viewModel.navigateBack(NavigatorType.Root) }
            )
        }

        ScreenStateLayout(
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() },
        ) { state ->
            val screenData = state.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    items(screenData.servers) { server ->
                        ServerSelectableItem(
                            server = server,
                            selected = screenData.selection == server,
                            onClick = { viewModel.selectServer(server) },
                        )
                    }
                }
                ButtonRegular(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.common_confirm),
                    enabled = screenData.formIsValid,
                    onClick = { viewModel.confirmSelection() },
                )
                Spacer(modifier = Modifier.height(20.dp))
                ButtonText(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.add_new_server),
                    onClick = { viewModel.openAddNewServer() },
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SelectServerBar(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        ToolbarPrimaryMount(
            leftInset = 40.dp,
            rightInset = 60.dp
        )
        Text(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.select_server),
            maxLines = 1,
            style = Gilroy32,
        )
    }
}
