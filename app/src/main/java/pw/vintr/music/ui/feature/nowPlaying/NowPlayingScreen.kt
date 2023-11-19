package pw.vintr.music.ui.feature.nowPlaying

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.PlayerStatusModel
import pw.vintr.music.tools.extension.Dash
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.kit.player.PlayerProgressBar
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun NowPlayingScreen(viewModel: NowPlayingViewModel = getViewModel()) {
    val playerState = viewModel.playerStateFlow.collectAsState()
    val progressState = viewModel.playerProgressFlow.collectAsState()

    playerState.value.currentTrack?.let { track ->
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl)
                .size(Size.ORIGINAL)
                .crossfade(enable = true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }

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
        ) {
            val title = playerState.value.currentTrack?.let { track ->
                buildString {
                    append(track.metadata.artist)
                    append(String.Space)
                    append(String.Dash)
                    append(String.Space)
                    append(track.metadata.title)
                }
            }.orEmpty()

            val description = playerState.value.currentTrack?.metadata?.album.orEmpty()

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
        PlayerProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            progress = progressState.value.progress,
            trackDuration = progressState.value.duration,
            onSeek = { viewModel.onSeek(it) },
            onSeekEnd = { viewModel.onSeekEnd() }
        )
        Box(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                .border(
                    1.dp,
                    VintrMusicExtendedTheme.colors.playerSliderStroke,
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SimpleIconButton(
                    iconRes = R.drawable.ic_skip_backward,
                    size = 40.dp,
                    iconModifier = Modifier.size(28.dp),
                    onClick = { viewModel.backward() }
                )
                Spacer(modifier = Modifier.width(20.dp))
                ButtonPlayerState(
                    isPlaying = playerState.value.status == PlayerStatusModel.PLAYING,
                    onClick = {
                        when (playerState.value.status) {
                            PlayerStatusModel.IDLE,
                            PlayerStatusModel.LOADING -> {
                                // TODO: action
                            }
                            PlayerStatusModel.PAUSED -> {
                                viewModel.resume()
                            }
                            PlayerStatusModel.PLAYING -> {
                                viewModel.pause()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                SimpleIconButton(
                    iconRes = R.drawable.ic_skip_forward,
                    size = 40.dp,
                    iconModifier = Modifier.size(28.dp),
                    onClick = { viewModel.forward() }
                )
            }
        }
    }
}
