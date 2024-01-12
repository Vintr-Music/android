package pw.vintr.music.ui.feature.library.artistList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.library.ArtistView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCells
import pw.vintr.music.ui.kit.layout.ScreenStateLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArtistListTab(
    viewModel: ArtistListViewModel = getViewModel()
) {
    val screenState = viewModel.screenState.collectAsState()

    ScreenStateLayout(
        state = screenState.value,
        errorRetryAction = { viewModel.loadData() }
    ) { state ->
        val artistList = state.data
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
                columns = rememberLibraryGridCells(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = artistList,
                    key = { it.name }
                ) { artist ->
                    ArtistView(
                        artist = artist,
                        onClick = { viewModel.onArtistClick(artist) }
                    )
                }
            }
        }
    }
}
