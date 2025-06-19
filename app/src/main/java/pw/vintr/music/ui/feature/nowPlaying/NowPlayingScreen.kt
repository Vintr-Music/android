package pw.vintr.music.ui.feature.nowPlaying

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap
import org.koin.androidx.compose.koinViewModel
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.composable.rememberDisplayRoundness
import pw.vintr.music.tools.extension.Dash
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.ui.kit.button.ButtonBorderedIcon
import pw.vintr.music.ui.kit.player.PlayerControls
import pw.vintr.music.ui.kit.player.PlayerProgressBar
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

private val MAX_CONTROLS_ROUNDNESS = 20.dp

@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = koinViewModel()
) {
    val playerState = viewModel.playerStateFlow.collectAsState().value
    val progressState = viewModel.playerProgressFlow.collectAsState()

    val baseControlsRoundness = rememberDisplayRoundness(MAX_CONTROLS_ROUNDNESS)
        .coerceAtMost(MAX_CONTROLS_ROUNDNESS)
    val semiControlRoundness = remember(baseControlsRoundness) { baseControlsRoundness / 2 }

    if (playerState.currentTrack != null) {
        // Create a pager state that's synced with our current track index
        val pagerState = rememberPagerState(
            initialPage = playerState.currentTrackIndex,
            pageCount = { playerState.session.tracks.size }
        )

        // Sync pager state with viewModel state when track changes programmatically
        LaunchedEffect(playerState.currentTrackIndex) {
            if (pagerState.currentPage != playerState.currentTrackIndex) {
                pagerState.animateScrollToPage(playerState.currentTrackIndex)
            }
        }

        // Handle user-initiated page changes
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                // Only notify if the page change was due to user interaction
                if (pagerState.isScrollInProgress) {
                    viewModel.seekToTrack(page)
                }
            }
        }

        // Artwork scrollable pager
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState
        ) { page ->
            val track = playerState.session.tracks[page]
            var bitmap by remember { mutableStateOf<Bitmap?>(value = null) }
            val accentColor = remember(bitmap) {
                bitmap?.let { lockedBitmap ->
                    Palette
                        .from(lockedBitmap)
                        .generate()
                        .mutedSwatch
                        ?.let { Color(it.rgb) }
                } ?: Color.Transparent
            }

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.artworkUrl)
                        .size(
                            height = constraints.maxHeight,
                            width = constraints.maxWidth
                        )
                        .crossfade(enable = true)
                        .allowHardware(enable = false)
                        .build(),
                    contentScale = ContentScale.Crop,
                    onSuccess = { bitmap = it.result.image.toBitmap() },
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(accentColor.copy(alpha = 0.5f)),
            )
        }
    }

    // Controls overlay
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background
                                .copy(alpha = 1.0f),
                            MaterialTheme.colorScheme.background
                                .copy(alpha = 0.0f),
                        )
                    )
                )
                .clickable {
                    playerState.currentTrack
                        ?.let { viewModel.openTrackAction(it) }
                }
        ) {
            val title = playerState.currentTrack?.let { track ->
                buildString {
                    append(track.metadata.artist)
                    append(String.Space)
                    append(String.Dash)
                    append(String.Space)
                    append(track.metadata.title)
                }
            }.orEmpty()

            val description = playerState.currentTrack?.metadata?.album.orEmpty()

            Text(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                text = title,
                style = Gilroy24,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = description,
                style = Gilroy16,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ButtonBorderedIcon(
                iconRes = R.drawable.ic_track_queue,
                onClick = { viewModel.openManageSession() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ButtonBorderedIcon(
                iconRes = R.drawable.ic_more_horizontal,
                onClick = {
                    playerState.currentTrack
                        ?.let { viewModel.openTrackAction(it) }
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        PlayerProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            RoundedCornerShape(semiControlRoundness),
            progress = progressState.value.progress,
            trackDuration = progressState.value.duration,
            onSeek = { viewModel.onSeek(it) },
            onSeekEnd = { viewModel.onSeekEnd() }
        )
        PlayerControls(
            playerState = playerState,
            shape = RoundedCornerShape(baseControlsRoundness),
            onBackward = { viewModel.backward() },
            onChangePlayerState = {
                when (playerState.status) {
                    PlayerStatusModel.IDLE,
                    PlayerStatusModel.LOADING,
                    PlayerStatusModel.PAUSED -> {
                        viewModel.resume()
                    }
                    PlayerStatusModel.PLAYING -> {
                        viewModel.pause()
                    }
                }
            },
            onForward = { viewModel.forward() },
            onSetNextRepeatMode = { viewModel.setNextRepeatMode(playerState.repeatMode) },
            onSetNextShuffleMode = { viewModel.setNextShuffleMode(playerState.shuffleMode) }
        )
    }
}
