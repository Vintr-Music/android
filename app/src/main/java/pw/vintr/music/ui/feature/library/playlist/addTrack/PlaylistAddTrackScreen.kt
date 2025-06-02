package pw.vintr.music.ui.feature.library.playlist.addTrack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.tools.composable.rememberBottomSheetNestedScrollInterceptor
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.button.ButtonSecondary
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.kit.state.EmptyState
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.theme.Green0
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun PlaylistAddTrackScreen(
    trackId: String,
    viewModel: PlaylistAddTrackViewModel = koinViewModel {
        parametersOf(trackId)
    },
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.playlist_add),
                showBackButton = false,
                trailing = {
                    ButtonSimpleIcon(
                        iconRes = R.drawable.ic_close,
                        onClick = { viewModel.navigateBack(NavigatorType.Root) },
                    )
                }
            )
        },
    ) { scaffoldPadding ->
        val screenState = viewModel.screenState.collectAsState()

        ScreenStateLayout(
            modifier = Modifier.padding(scaffoldPadding),
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() },
            empty = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldPadding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    EmptyState(
                        modifier = Modifier
                            .fillMaxWidth(),
                        iconRes = R.drawable.ic_empty_playlist,
                        text = stringResource(id = R.string.playlist_empty),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonRegular(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        text = stringResource(id = R.string.playlist_create),
                        onClick = { viewModel.openCreatePlaylist() }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            },
            loaded = { state ->
                val lazyListState = rememberLazyListState()
                val items = state.data

                Column(
                    modifier = Modifier
                        .nestedScroll(
                            rememberBottomSheetNestedScrollInterceptor(
                                lazyListState = lazyListState
                            )
                        )
                        .fillMaxSize()
                        .padding(scaffoldPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 20.dp)
                    ) {
                        items(
                            items = items,
                            key = { item -> item.playlist.id }
                        ) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !item.containsTargetTrack) {
                                        viewModel.addTrackToPlaylist(item.playlist)
                                    }
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BoxWithConstraints(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .artworkContainer(RoundedCornerShape(4.dp)),
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(item.playlist.artworkUrl)
                                            .size(
                                                width = constraints.maxWidth,
                                                height = constraints.maxHeight
                                            )
                                            .memoryCachePolicy(CachePolicy.DISABLED)
                                            .diskCachePolicy(CachePolicy.DISABLED)
                                            .error(R.drawable.ic_playlist_no_artwork)
                                            .crossfade(enable = true)
                                            .build(),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.playlist.name,
                                        style = RubikMedium16,
                                        color = VintrMusicExtendedTheme.colors.textRegular
                                    )
                                    if (item.playlist.description.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.playlist.description,
                                            style = RubikRegular14,
                                            color = VintrMusicExtendedTheme.colors.textSecondary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                when {
                                    item.isLoading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = VintrMusicExtendedTheme.colors.textRegular,
                                        )
                                    }
                                    item.containsTargetTrack -> {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            painter = painterResource(id = R.drawable.ic_check),
                                            tint = Green0,
                                            contentDescription = null
                                        )
                                    }
                                    else -> {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            painter = painterResource(id = R.drawable.ic_add),
                                            tint = VintrMusicExtendedTheme.colors.textRegular,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    ButtonSecondary(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = stringResource(id = R.string.playlist_new),
                        onClick = { viewModel.openCreatePlaylist() },
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        )
    }
}
