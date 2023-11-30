package pw.vintr.music.ui.feature.nowPlaying

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.state.PlayerProgressModel
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.ui.base.BaseViewModel

class NowPlayingViewModel(
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    data class SeekSnapshot(
        val value: Float = 0f,
        val isSeeking: Boolean = false,
        val seekMediaId: String? = null,
    )

    val playerStateFlow = playerInteractor.playerState
        .stateInThis(PlayerStateHolderModel())

    private val seekProgress = MutableStateFlow(value = SeekSnapshot())

    val playerProgressFlow = combine(
        playerInteractor.playProgressState,
        seekProgress
    ) { playerProgress, seekSnapshot ->
        val isLoading = playerProgress.isLoading
        val seekCurrentMedia = seekSnapshot.seekMediaId == playerProgress.mediaId

        val progress = when {
            seekSnapshot.isSeeking -> {
                seekSnapshot.value
            }
            playerProgress.isOnStart -> {
                playerProgress.progress
            }
            isLoading && seekCurrentMedia -> {
                seekSnapshot.value
            }
            else -> {
                playerProgress.progress
            }
        }

        playerProgress.copy(progress = progress)
    }.stateInThis(PlayerProgressModel())

    fun pause() {
        playerInteractor.pause()
    }

    fun resume() {
        playerInteractor.resume()
    }

    fun backward() {
        playerInteractor.backward()
    }

    fun forward() {
        playerInteractor.forward()
    }

    fun setNextRepeatMode(currentRepeatMode: PlayerRepeatMode) {
        val nextMode = when (currentRepeatMode) {
            PlayerRepeatMode.OFF -> PlayerRepeatMode.ON_SESSION
            PlayerRepeatMode.ON_SINGLE -> PlayerRepeatMode.OFF
            PlayerRepeatMode.ON_SESSION -> PlayerRepeatMode.ON_SINGLE
        }

        playerInteractor.setRepeatMode(nextMode)
    }

    fun setNextShuffleMode(currentShuffleMode: PlayerShuffleMode) {
        val nextMode = when (currentShuffleMode) {
            PlayerShuffleMode.OFF -> PlayerShuffleMode.ON
            PlayerShuffleMode.ON -> PlayerShuffleMode.OFF
        }

        playerInteractor.setShuffleMode(nextMode)
    }

    fun onSeek(value: Float) {
        seekProgress.value = SeekSnapshot(
            value = value,
            isSeeking = true,
            seekMediaId = playerStateFlow.value.currentTrack?.md5,
        )
    }

    fun onSeekEnd() {
        playerInteractor.seekTo(seekProgress.value.value.toLong())
        launch {
            delay(timeMillis = 500)
            seekProgress.update { it.copy(isSeeking = false) }
        }
    }
}
