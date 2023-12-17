package pw.vintr.music.ui.feature.equalizer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import pw.vintr.music.tools.extension.scaffoldPadding
import pw.vintr.music.ui.kit.equalizer.EqualizerBandSlider
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.menu.MenuItemSwitchable
import pw.vintr.music.ui.kit.menu.MenuSectionTitle
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun EqualizerScreen(viewModel: EqualizerViewModel = getViewModel()) {
    val screenState = viewModel.screenState.collectAsState()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.equalizer),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) { scaffoldPadding ->
        ScreenStateLayout(
            modifier = Modifier.padding(scaffoldPadding),
            state = screenState.value,
            errorRetryAction = { viewModel.initializeEqualizer() },
        ) { state ->
            val equalizerData = state.data

            Column(
                modifier = Modifier
                    .scaffoldPadding(scaffoldPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp)
            ) {
                MenuItemSwitchable(
                    modifier = Modifier
                        .clickable { viewModel.changeUseEqualizer(!equalizerData.enabled) }
                        .padding(horizontal = 20.dp),
                    title = stringResource(id = R.string.use_equalizer),
                    checked = equalizerData.enabled,
                    onCheckedChange = { viewModel.changeUseEqualizer(it) }
                )
                Spacer(modifier = Modifier.height(40.dp))
                MenuSectionTitle(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    title = stringResource(id = R.string.manual_tuning),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    equalizerData.bands.forEach { band ->
                        EqualizerBandSlider(
                            modifier = Modifier.weight(1f),
                            isActive = equalizerData.enabled,
                            currentLevel = band.currentLevel.toFloat(),
                            lowerLevel = band.lowerLevel.toFloat(),
                            upperLevel = band.upperLevel.toFloat(),
                            centerFrequency = band.centerFrequency,
                            onValueChange = { viewModel.changeBandLevel(band, it) },
                            onValueChangeFinished = { viewModel.applyChanges() }
                        )
                    }
                }
            }
        }
    }
}
