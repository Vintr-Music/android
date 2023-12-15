package pw.vintr.music.ui.feature.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.BuildConfig
import pw.vintr.music.R
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.modifier.cardContainer
import pw.vintr.music.ui.kit.server.ServerItem
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy32
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = getViewModel()
) {
    val screenState = viewModel.screenState.collectAsState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        ScreenStateLayout(
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() },
        ) { state ->
            val screenData = state.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Profile Info
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = screenData.user.fullName,
                    style = Gilroy32,
                    color = VintrMusicExtendedTheme.colors.textRegular,
                )
                Spacer(modifier = Modifier.height(28.dp))

                // Selected server info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .cardContainer(onClick = { viewModel.openServerSelection() }),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.selected_server),
                            style = Gilroy16,
                            color = VintrMusicExtendedTheme.colors.textRegular
                        )
                        ServerItem(server = screenData.server)
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_forward),
                        contentDescription = null,
                        tint = Bee0,
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))

                // Menu items
                MenuItemIconified(
                    modifier = Modifier
                        .clickable { viewModel.openSettings() }
                        .padding(horizontal = 28.dp, vertical = 8.dp),
                    title = stringResource(id = R.string.settings),
                    subtitle = stringResource(id = R.string.settings_description),
                    iconRes = R.drawable.ic_settings,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemIconified(
                    modifier = Modifier
                        .clickable { viewModel.openAbout() }
                        .padding(horizontal = 28.dp, vertical = 8.dp),
                    title = stringResource(id = R.string.about_title),
                    subtitle = stringResource(
                        id = R.string.about_description,
                        BuildConfig.VERSION_NAME
                    ),
                    iconRes = R.drawable.ic_info,
                )
            }
        }
    }
}
