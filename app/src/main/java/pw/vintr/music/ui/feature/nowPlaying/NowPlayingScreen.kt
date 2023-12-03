package pw.vintr.music.ui.feature.nowPlaying

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.Dash
import pw.vintr.music.tools.extension.Space
import pw.vintr.music.ui.kit.button.ButtonPlayerState
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.kit.player.PlayerProgressBar
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun NowPlayingScreen(viewModel: NowPlayingViewModel = getViewModel()) {
    val playerState = viewModel.playerStateFlow.collectAsState()
    val progressState = viewModel.playerProgressFlow.collectAsState()

    playerState.value.currentTrack?.let { track ->
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
                onSuccess = { bitmap = it.result.drawable.toBitmap() },
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(accentColor.copy(alpha = 0.5f)),
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
                    iconRes = when (playerState.value.repeatMode) {
                        PlayerRepeatMode.OFF,
                        PlayerRepeatMode.ON_SESSION -> R.drawable.ic_repeat
                        PlayerRepeatMode.ON_SINGLE -> R.drawable.ic_repeat_single
                    },
                    size = 40.dp,
                    iconModifier = Modifier.size(28.dp),
                    tint = when (playerState.value.repeatMode) {
                        PlayerRepeatMode.OFF -> VintrMusicExtendedTheme.colors.textRegular
                        PlayerRepeatMode.ON_SESSION,
                        PlayerRepeatMode.ON_SINGLE -> Bee0
                    },
                    onClick = { viewModel.setNextRepeatMode(playerState.value.repeatMode) },
                )
                Spacer(modifier = Modifier.width(20.dp))
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
                            PlayerStatusModel.LOADING,
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
                Spacer(modifier = Modifier.width(20.dp))
                SimpleIconButton(
                    iconRes = R.drawable.ic_shuffle,
                    size = 40.dp,
                    iconModifier = Modifier.size(28.dp),
                    tint = when (playerState.value.shuffleMode) {
                        PlayerShuffleMode.OFF -> VintrMusicExtendedTheme.colors.textRegular
                        PlayerShuffleMode.ON -> Bee0
                    },
                    onClick = { viewModel.setNextShuffleMode(playerState.value.shuffleMode) },
                )
            }
        }
    }
}
