package pw.vintr.music.domain.player.interactor

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import pw.vintr.music.app.service.VintrMusicService
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.PlayerState

class PlayerInteractor(
    applicationContext: Context
) {
    private val sessionToken = SessionToken(
        applicationContext,
        ComponentName(applicationContext, VintrMusicService::class.java)
    )

    private val controllerFuture = MediaController
        .Builder(applicationContext, sessionToken)
        .buildAsync()

    private var controller: MediaController? = null

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Idle)

    val playerState = _playerState.asSharedFlow()

    init {
        controllerFuture.addListener({
            controller = controllerFuture.get()

            controller?.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    if (
                        events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) ||
                        events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
                    ) {
                        val mediaId = player.currentMediaItem?.mediaId

                        _playerState.value = when {
                            mediaId != null && player.isPlaying -> {
                                PlayerState.Playing(trackId = mediaId)
                            }
                            mediaId != null -> {
                                PlayerState.Paused(trackId = mediaId)
                            }
                            else -> {
                                PlayerState.Idle
                            }
                        }
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    fun invokePlay(tracks: List<TrackModel>, startIndex: Int = 0) {
        controller?.stop()
        controller?.setMediaItems(
            tracks.map { it.toMediaItem() },
            startIndex,
            0
        )
        controller?.play()
    }

    fun invokePause() {
        controller?.pause()
    }

    private fun TrackModel.toMediaItem() = MediaItem.Builder()
        .setUri(playerUrl)
        .setMediaId(md5)
        .build()
}
