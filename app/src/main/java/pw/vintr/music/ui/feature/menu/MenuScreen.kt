package pw.vintr.music.ui.feature.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.loader.LoaderScreen
import pw.vintr.music.ui.kit.menu.MenuItemIconified
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
        when (val state = screenState.value) {
            is MenuState.Loading -> {
                LoaderScreen()
            }
            is MenuState.Error -> {
                // TODO: error state
            }
            is MenuState.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = state.user.fullName,
                        style = Gilroy32,
                        color = VintrMusicExtendedTheme.colors.textRegular,
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    MenuItemIconified(
                        modifier = Modifier
                            .clickable { viewModel.openSettings() }
                            .padding(horizontal = 28.dp, vertical = 8.dp),
                        title = stringResource(id = R.string.settings),
                        subtitle = stringResource(id = R.string.settings_description),
                        iconRes = R.drawable.ic_settings,
                    )
                }
            }
        }
    }
}
