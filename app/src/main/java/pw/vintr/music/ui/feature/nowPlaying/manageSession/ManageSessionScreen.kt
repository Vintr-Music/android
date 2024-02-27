package pw.vintr.music.ui.feature.nowPlaying.manageSession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType

@Composable
fun ManageSessionScreen(
    viewModel: ManageSessionViewModel = getViewModel(),
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.now_playing),
                showBackButton = false,
                trailing = {
                    ButtonSimpleIcon(
                        iconRes = R.drawable.ic_close,
                        onClick = { viewModel.navigateBack(NavigatorType.Root) },
                    )
                }
            )
        },
    ) {
        val screenData = viewModel.screenData.collectAsState()

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(
                items = screenData.value.session.tracks,
                key = { _, track -> track.md5 }
            ) { index, track ->
                TrackView(
                    trackModel = track,
                    isPlaying = screenData.value.currentTrack == track,
                    showTrailingAction = false,
                    onClick = { viewModel.seekToTrack(index) }
                )
            }
        }
    }
}
