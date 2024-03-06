package pw.vintr.music.ui.feature.library.artistList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.library.ArtistView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCells
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArtistListScreen(
    viewModel: ArtistListViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.all_artists),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) { scaffoldPadding ->
        val screenState = viewModel.screenState.collectAsState()

        ScreenStateLayout(
            modifier = Modifier.padding(scaffoldPadding),
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() }
        ) { state ->
            val artistList = state.data
            val pullRefreshState = rememberPullRefreshState(
                refreshing = state.isRefreshing,
                onRefresh = { viewModel.refreshData() }
            )

            PullRefreshLayout(
                modifier = Modifier
                    .scaffoldPadding(scaffoldPadding),
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
                        key = { artist -> artist.name }
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
}
