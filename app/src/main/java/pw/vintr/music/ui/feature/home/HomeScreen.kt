package pw.vintr.music.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import pw.vintr.music.domain.mainPage.model.MainPageWelcomeModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.visualizer.model.VisualizerState
import pw.vintr.music.ui.kit.layout.PullRefreshLayout
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.library.tools.rememberLibraryGridCellsCount
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.modifier.artworkGradientOverlayBackground
import pw.vintr.music.ui.kit.player.FlowPlayer
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayout
import pw.vintr.music.ui.kit.toolbar.collapsing.CollapsingLayoutState
import pw.vintr.music.ui.kit.toolbar.collapsing.LocalCollapsingScrollConnection
import pw.vintr.music.ui.kit.toolbar.collapsing.ScrollStrategy
import pw.vintr.music.ui.kit.toolbar.collapsing.collapsingToolbarScrollConnection
import pw.vintr.music.ui.kit.toolbar.collapsing.rememberCollapsingLayoutState
import pw.vintr.music.ui.kit.visualizer.Visualizer
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val screenState = viewModel.screenState.collectAsState()
    val visualizerData = viewModel.visualizerState.collectAsState()
    val flowPlayingState = viewModel.flowPlayingState.collectAsState()

    val collapsingLayoutState = rememberCollapsingLayoutState()
    val cellsCount = rememberLibraryGridCellsCount()
    val gridState = rememberLazyGridState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
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
                CollapsingLayout(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = collapsingLayoutState,
                    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                    toolbar = {
                        val nestedScrollConnection = LocalCollapsingScrollConnection.current

                        HomeHeader(
                            modifier = Modifier
                                .collapsingToolbarScrollConnection(
                                    coroutineScope = coroutineScope,
                                    connection = nestedScrollConnection
                                ),
                            collapsingState = collapsingLayoutState,
                            welcomeState = screenData.welcome,
                            flowPlayingState = flowPlayingState.value,
                            visualizerState = visualizerData.value,
                            onHeaderSizeChanged = {
                                if (collapsingLayoutState.toolbarState.progress != 0f) {
                                    coroutineScope.launch {
                                        collapsingLayoutState.toolbarState.expand()
                                    }
                                }
                            },
                            onChangeFlowPlayerState = {
                                when (flowPlayingState.value.playerStatus) {
                                    PlayerStatusModel.IDLE,
                                    PlayerStatusModel.PAUSED -> {
                                        viewModel.playFlow()
                                    }
                                    PlayerStatusModel.PLAYING -> {
                                        viewModel.pauseFlow()
                                    }
                                    else -> Unit
                                }
                            },
                            onShuffleFlowClick = {
                                viewModel.playNewFlow()
                            }
                        )
                    }
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = gridState,
                        columns = GridCells.Fixed(cellsCount),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        // Artists compilation
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
                            items(
                                items = item.albums,
                                key = { it.id }
                            ) { album ->
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
}

@Composable
private fun HomeHeader(
    modifier: Modifier = Modifier,
    collapsingState: CollapsingLayoutState,
    welcomeState: MainPageWelcomeModel,
    flowPlayingState: FlowPlayingState,
    visualizerState: VisualizerState,
    onHeaderSizeChanged: () -> Unit = {},
    onChangeFlowPlayerState: () -> Unit = {},
    onShuffleFlowClick: () -> Unit = {},
) {
    val currentTrack = flowPlayingState.playingTrack

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onSizeChanged { onHeaderSizeChanged() }
    ) {
        // Album artwork as a background
        if (currentTrack != null) {
            AsyncImage(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(collapsingState.toolbarState.progress),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentTrack.artworkUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .artworkGradientOverlayBackground(top = 0.5f)
            )
        }

        // Header's content
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Spacer(Modifier.height(20.dp))
            // Welcome text
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = welcomeState.textRes),
                style = Gilroy24,
                color = VintrMusicExtendedTheme.colors.textRegular,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(36.dp))

            // Music flow controls
            FlowPlayer(
                playerStatus = flowPlayingState.playerStatus,
                currentSessionIsFlow = flowPlayingState.currentIsFlow,
                onChangePlayerState = onChangeFlowPlayerState,
                onShuffleClick = onShuffleFlowClick
            )
            Spacer(Modifier.height(28.dp))

            // Visualizer
            if (visualizerState.enabled) {
                Visualizer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    bytes = visualizerState.bytes,
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Status-bar-sized box for collapsed size determination
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    )
}
