package pw.vintr.music.ui.feature.serverSelection.connectNewServer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import pw.vintr.music.tools.extension.scaffoldPadding
import pw.vintr.music.ui.kit.selector.SegmentControl
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun ConnectNewServerScreen(
    viewModel: ConnectNewServerViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.add_new_server),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scaffoldPadding(scaffoldPadding)
        ) {
            val screenState = viewModel.screenState.collectAsState()

            SegmentControl(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(36.dp),
                items = ConnectNewServerTabType.records
                    .map { stringResource(id = it.tabTitleRes) },
                selectedItemIndex = screenState.value.tabData.tabType.ordinal,
                onSelectedTab = { viewModel.selectTab(ConnectNewServerTabType.records[it]) }
            )
        }
    }
}
