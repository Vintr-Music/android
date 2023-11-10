package pw.vintr.music.ui.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.feature.library.artistList.ArtistListTab
import pw.vintr.music.ui.kit.tab.AppScrollableTabRow
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = getViewModel()
) {
    val screenState = viewModel.screenState.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = stringResource(id = R.string.library),
            style = Gilroy24,
            color = VintrMusicExtendedTheme.colors.textRegular,
        )
        AppScrollableTabRow(
            tabs = screenState.value.tabs.map {
                stringResource(id = it.titleRes)
            },
            selectedTabIndex = screenState.value.selectedIndex,
            onTabClick = { viewModel.selectTab(screenState.value.tabs[it]) }
        )
        when (screenState.value.selectedTab) {
            LibraryTab.ARTISTS -> {
                ArtistListTab()
            }
            LibraryTab.ALBUMS -> {
                // TODO: album tab
            }
        }
    }
}
