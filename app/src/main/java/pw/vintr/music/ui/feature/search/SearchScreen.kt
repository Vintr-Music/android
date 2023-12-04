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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
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

private const val MAX_DISPLAY_ARTISTS = 9
private const val MAX_DISPLAY_ALBUMS = 9

@Composable
fun SearchScreen(viewModel: SearchViewModel = getViewModel()) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val contentState = viewModel.contentState.collectAsState()
        val queryState = viewModel.queryState.collectAsState()

        Spacer(modifier = Modifier.height(20.dp))
        AppTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            hint = stringResource(id = R.string.search_title),
            value = queryState.value,
            onValueChange = { viewModel.changeQuery(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.Sentences
            ),
            actionOnDone = { viewModel.performSearch() },
            leadingIconRes = R.drawable.ic_search
        )

        ScreenStateLayout(
            state = contentState.value,
            loaded = { content ->
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Fixed(count = 3),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(all = 20.dp)
                ) {
                    // Artists
                    if (content.artists.isNotEmpty()) {
                        item(
                            key = TAG_ARTISTS_TITLE,
                            span = { GridItemSpan(currentLineSpan = 3) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.search_artists_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            content.artists.take(MAX_DISPLAY_ARTISTS)
                        ) { artist ->
                            ArtistView(
                                artist = artist,
                                onClick = { viewModel.onArtistClick(artist) }
                            )
                        }
                    }

                    // Albums
                    if (content.albums.isNotEmpty()) {
                        item(
                            key = TAG_ALBUMS_TITLE,
                            span = { GridItemSpan(currentLineSpan = 3) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.search_albums_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            content.albums.take(MAX_DISPLAY_ALBUMS)
                        ) { album ->
                            AlbumView(
                                album = album,
                                onClick = { viewModel.onAlbumClick(album) }
                            )
                        }
                    }

                    // Tracks
                    if (content.tracks.isNotEmpty()) {
                        item(
                            key = TAG_TRACKS_TITLE,
                            span = { GridItemSpan(currentLineSpan = 3) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.search_albums_title),
                                style = Gilroy18,
                                color = VintrMusicExtendedTheme.colors.textRegular
                            )
                        }
                        items(
                            content.tracks,
                            span = { GridItemSpan(currentLineSpan = 3) }
                        ) { track ->
                            TrackView(
                                trackModel = track,
                                contentPadding = PaddingValues(vertical = 4.dp),
                                onClick = {
                                    viewModel.onTrackClicked(
                                        tracks = content.tracks,
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
