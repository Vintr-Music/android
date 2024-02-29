package pw.vintr.music.ui.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.extension.escapePadding
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.AlbumView
import pw.vintr.music.ui.kit.library.ArtistView
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private const val TAG_ARTISTS_TITLE = "tag-artists-title"
private const val TAG_ALBUMS_TITLE = "tag-albums-title"
private const val TAG_TRACKS_TITLE = "tag-tracks-title"

private const val MAX_DISPLAY_ARTISTS = 6
private const val MAX_DISPLAY_ALBUMS = 6

private const val COLUMN_COUNT = 3

private const val SPACING_DP = 20

@Composable
fun SearchScreen(viewModel: SearchViewModel = getViewModel()) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val contentState = viewModel.screenState.collectAsState()
        val queryState = viewModel.queryState.collectAsState()
        val playerState = viewModel.playerState.collectAsState()

        val focusRequester = remember { FocusRequester() }

        Spacer(modifier = Modifier.height(20.dp))
        AppTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            textFieldModifier = Modifier
                .focusRequester(focusRequester),
            hint = stringResource(id = R.string.search_title),
            value = queryState.value,
            onValueChange = { viewModel.changeQuery(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.Sentences
            ),
            showClearButton = queryState.value.isNotEmpty(),
            actionOnClear = {
                viewModel.clearSearch()
                focusRequester.requestFocus()
            },
            actionOnDone = { viewModel.performSearch() },
            leadingIconRes = R.drawable.ic_search
        )

        ScreenStateLayout(
            state = contentState.value,
            loaded = { state ->
                val screenData = state.data

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Fixed(count = COLUMN_COUNT),
                    verticalArrangement = Arrangement.spacedBy(SPACING_DP.dp),
                    horizontalArrangement = Arrangement.spacedBy(SPACING_DP.dp),
                    contentPadding = PaddingValues(
                        start = SPACING_DP.dp,
                        end = SPACING_DP.dp,
                        bottom = SPACING_DP.dp
                    )
                ) {
                    // Artists
                    if (screenData.artists.isNotEmpty()) {
                        item(
                            key = TAG_ARTISTS_TITLE,
                            span = { GridItemSpan(currentLineSpan = COLUMN_COUNT) }
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 20.dp),
                                text = stringResource(id = R.string.search_artists_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            screenData.artists.take(MAX_DISPLAY_ARTISTS),
                            key = { it.name }
                        ) { artist ->
                            ArtistView(
                                artist = artist,
                                onClick = { viewModel.onArtistClick(artist) }
                            )
                        }
                    }

                    // Albums
                    if (screenData.albums.isNotEmpty()) {
                        item(
                            key = TAG_ALBUMS_TITLE,
                            span = { GridItemSpan(currentLineSpan = COLUMN_COUNT) }
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 20.dp),
                                text = stringResource(id = R.string.search_albums_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            screenData.albums.take(MAX_DISPLAY_ALBUMS),
                            key = { it.id }
                        ) { album ->
                            AlbumView(
                                album = album,
                                onClick = { viewModel.onAlbumClick(album) }
                            )
                        }
                    }

                    // Tracks
                    if (screenData.tracks.isNotEmpty()) {
                        item(
                            key = TAG_TRACKS_TITLE,
                            span = { GridItemSpan(currentLineSpan = COLUMN_COUNT) }
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 20.dp),
                                text = stringResource(id = R.string.search_tracks_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            screenData.tracks,
                            span = { GridItemSpan(currentLineSpan = COLUMN_COUNT) },
                            key = { it.md5 }
                        ) { track ->
                            TrackView(
                                modifier = Modifier
                                    .escapePadding(horizontal = SPACING_DP.dp),
                                trackModel = track,
                                isPlaying = playerState.value.currentTrack == track,
                                showArtwork = true,
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                onMoreClick = {
                                    viewModel.openTrackDetails(track)
                                },
                                onClick = {
                                    viewModel.onTrackClicked(
                                        tracks = screenData.tracks,
                                        track = track
                                    )
                                }
                            )
                        }
                    }
                }
            },
            other = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier.size(124.dp),
                        painter = painterResource(id = R.drawable.ic_search_large),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.search_no_history_hint),
                        color = VintrMusicExtendedTheme.colors.textRegular,
                        style = Gilroy18
                    )
                }
            }
        )
    }
}
