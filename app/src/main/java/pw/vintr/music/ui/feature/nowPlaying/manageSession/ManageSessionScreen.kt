package pw.vintr.music.ui.feature.nowPlaying.manageSession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

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
        val screenData = viewModel.screenState.collectAsState()
        val sessionIsEmpty = screenData.value.uiTracks.isEmpty()

        val reorderState = rememberReorderableLazyListState(
            onMove = { from, to ->
                viewModel.reorder(from.index, to.index)
            },
            onDragEnd = { _, _ ->
                viewModel.saveReorder()
            }
        )
        val listState = reorderState.listState

        LaunchedEffect(key1 = sessionIsEmpty) {
            if (!sessionIsEmpty) {
                val currentTrackIndex = screenData.value.uiTracks
                    .indexOfFirst { wrapper -> screenData.value.currentTrack == wrapper.track }

                if (currentTrackIndex != -1) {
                    listState.scrollToItem(currentTrackIndex)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .reorderable(reorderState),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
        ) {
            itemsIndexed(
                items = screenData.value.uiTracks,
                key = { _, wrapper -> wrapper.listUUID }
            ) { index, wrapper ->
                ReorderableItem(
                    reorderableState = reorderState,
                    key = wrapper.listUUID,
                ) {
                    TrackView(
                        trackModel = wrapper.track,
                        isPlaying = screenData.value.currentTrack == wrapper.track,
                        trailingAction = {
                            Icon(
                                modifier = Modifier
                                    .detectReorder(reorderState),
                                painter = painterResource(id = R.drawable.ic_drag),
                                tint = VintrMusicExtendedTheme.colors.textRegular,
                                contentDescription = null
                            )
                        },
                        onClick = { viewModel.seekToTrack(index) }
                    )
                }
            }
        }
    }
}
