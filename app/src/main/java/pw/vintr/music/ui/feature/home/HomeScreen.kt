package pw.vintr.music.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCellsCount
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val KEY_WELCOME_TEXT = "welcome-text"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel()
) {
    val screenState = viewModel.screenState.collectAsState()
    val cellsCount = rememberLibraryGridCellsCount()

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
            val pullRefreshState = rememberPullRefreshState(
                refreshing = state.isRefreshing,
                onRefresh = { viewModel.refreshData() }
            )

            PullRefreshLayout(
                state = pullRefreshState,
                refreshing = state.isRefreshing
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Fixed(cellsCount),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item(
                        key = KEY_WELCOME_TEXT,
                        span = { GridItemSpan(currentLineSpan = cellsCount) },
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 20.dp),
                            text = stringResource(id = screenData.welcome.textRes),
                            style = Gilroy24,
                            color = VintrMusicExtendedTheme.colors.textRegular,
                        )
                    }

                    screenData.items.forEach { item ->
                        item(
                            key = item.artist,
                            span = { GridItemSpan(currentLineSpan = cellsCount) },
                        ) {
                            Text(
                                text = item.artist,
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular,
                            )
                        }
                        items(item.albums) { album ->
                            AlbumView(
                                album = album,
                                onClick = { viewModel.onAlbumClick(album) }
                            )
                        }
                    }
                }
            }
        }
    }
}
