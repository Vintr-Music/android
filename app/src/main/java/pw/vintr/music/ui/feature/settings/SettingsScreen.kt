package pw.vintr.music.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.menu.MenuItem
import pw.vintr.music.ui.kit.menu.MenuItemSwitchable
import pw.vintr.music.ui.kit.menu.MenuSectionTitle
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.settings),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) {
        val screenState = viewModel.screenState.collectAsState()

        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
        ) {
            MenuSectionTitle(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                title = stringResource(id = R.string.settings_playback_section),
            )
            Spacer(modifier = Modifier.height(14.dp))
            MenuItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openEqualizer() }
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                title = stringResource(id = R.string.equalizer),
                subtitle = stringResource(id = R.string.equalizer_description)
            )
            Spacer(modifier = Modifier.height(14.dp))
            MenuItemSwitchable(
                modifier = Modifier
                    .clickable {
                        viewModel.setNeedSpeakerNotification(
                            !screenState.value.needSpeakerNotification
                        )
                    }
                    .padding(horizontal = 20.dp),
                title = stringResource(id = R.string.settings_speaker_notification_title),
                subtitle = stringResource(id = R.string.settings_speaker_notification_message),
                checked = screenState.value.needSpeakerNotification,
                onCheckedChange = { value -> viewModel.setNeedSpeakerNotification(value) }
            )
        }
    }
}
