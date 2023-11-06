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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.state.ScreenStateHolder
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val KEY_WELCOME_TEXT = "welcome-text"

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = getViewModel()
) {
    val screenState = viewModel.screenState.collectAsState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        ScreenStateHolder(
            state = screenState.value,
        ) { state ->
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                item(
                    key = KEY_WELCOME_TEXT,
                    span = { GridItemSpan(currentLineSpan = 2) },
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = stringResource(id = state.welcome.textRes),
                        style = Gilroy24,
                        color = VintrMusicExtendedTheme.colors.textRegular,
                    )
                }

                state.items.forEach { item ->
                    item(
                        key = item.artist,
                        span = { GridItemSpan(currentLineSpan = 2) },
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
