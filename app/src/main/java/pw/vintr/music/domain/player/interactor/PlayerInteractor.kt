package pw.vintr.music.domain.player.interactor

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import pw.vintr.music.app.service.VintrMusicService
import pw.vintr.music.data.player.repository.PlayerConfigRepository
import pw.vintr.music.data.player.repository.PlayerSessionRepository
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.state.PlayerProgressModel
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.player.model.session.toModel
import java.io.Closeable

class PlayerInteractor(
    applicationContext: Context,
    private val playerSessionRepository: PlayerSessionRepository,
    private val playerConfigRepository: PlayerConfigRepository,
) : CoroutineScope, Closeable {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + job

    private val sessionToken = SessionToken(
        applicationContext,
        ComponentName(applicationContext, VintrMusicService::class.java)
    )

    private val controllerFuture = MediaController
        .Builder(applicationContext, sessionToken)
        .buildAsync()

    private var controller: MediaController? = null

    private val playerSnapshotFlow = MutableStateFlow(
        PlayerSnapshot(
            repeatMode = PlayerRepeatMode.getByCode(playerConfigRepository.getRepeatMode()),
            shuffleMode = PlayerShuffleMode.getByCode(playerConfigRepository.getShuffleMode())
        )
    )

    val playerState = combine(
        playerSessionRepository.getPlayerSessionFlow(),
        playerSnapshotFlow,
    ) { sessionCache, snapshot ->
        val session = sessionCache?.toModel() ?: PlayerSessionModel.Empty
        val track = session.tracks.find { it.md5 == snapshot.mediaId }

        PlayerStateHolderModel(
            session = session,
            currentTrack = track,
            status = snapshot.status,
            repeatMode = snapshot.repeatMode,
            shuffleMode = snapshot.shuffleMode
        )
    }.shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    val playProgressState = flow {
        while (isActive) {
            val mediaId = controller?.currentMediaItem?.mediaId
            val position = controller?.currentPosition?.toFloat() ?: 0f
            val duration = controller?.duration?.toFloat() ?: 0f

            if (position >= 0 && duration >= 0) {
                emit(
                    PlayerProgressModel(
                        progress = position,
                        duration = duration,
                        mediaId = mediaId,
                        isLoading = controller?.isPlaying != true &&
                                controller?.isLoading == true
                    )
                )
            }
            delay(timeMillis = 500)
        }
    }.shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    init {
        controllerFuture.addListener({
            controller = controllerFuture.get()

            controller?.repeatMode = playerSnapshotFlow.value.repeatMode
                .toSystemRepeatMode()
            controller?.shuffleModeEnabled = playerSnapshotFlow.value.shuffleMode
                .toSystemShuffleMode()

            controller?.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    onPlayerEvent(player)
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun onPlayerEvent(player: Player) {
        val mediaId = player.currentMediaItem?.mediaId
        val status = when {
            mediaId != null && player.isPlaying -> {
                PlayerStatusModel.PLAYING
            }
            mediaId != null && player.isLoading -> {
                PlayerStatusModel.LOADING
            }
            mediaId != null -> {
                PlayerStatusModel.PAUSED
            }
            player.isLoading -> {
                PlayerStatusModel.LOADING
            }
            else -> {
                PlayerStatusModel.IDLE
            }
        }

        playerSnapshotFlow.update { snapshot ->
            snapshot.copy(
                mediaId = mediaId,
                status = status
            )
        }
    }

    suspend fun playAlbum(
        tracks: List<TrackModel>,
        album: AlbumModel,
        startIndex: Int = 0
    ) {
        controller?.stop()

        playerSessionRepository.savePlayerSession(
            session = PlayerSessionModel.Album(
                album = album,
                tracks = tracks
            ).toCacheObject()
        )

        controller?.setMediaItems(
            tracks.map { it.toMediaItem() },
            startIndex,
            0
        )
        controller?.play()
    }

    suspend fun playQueue(tracks: List<TrackModel>, startIndex: Int = 0) {
        controller?.stop()

        playerSessionRepository.savePlayerSession(
            session = PlayerSessionModel.Custom(tracks).toCacheObject()
        )

        controller?.setMediaItems(
            tracks.map { it.toMediaItem() },
            startIndex,
            0
        )
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun resume() {
        controller?.play()
    }

    fun backward() {
        controller?.seekToPrevious()
    }

    fun forward() {
        controller?.seekToNextMediaItem()
    }

    fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    fun setRepeatMode(repeatMode: PlayerRepeatMode) {
        playerConfigRepository.setRepeatMode(repeatMode.ordinal)
        controller?.repeatMode = repeatMode.toSystemRepeatMode()
        playerSnapshotFlow.update { snapshot -> snapshot.copy(repeatMode = repeatMode) }
    }

    fun setShuffleMode(shuffleMode: PlayerShuffleMode) {
        playerConfigRepository.setShuffleMode(shuffleMode.ordinal)
        controller?.shuffleModeEnabled = shuffleMode.toSystemShuffleMode()
        playerSnapshotFlow.update { snapshot -> snapshot.copy(shuffleMode = shuffleMode) }
    }

    private fun TrackModel.toMediaItem() = MediaItem.Builder()
        .setUri(playerUrl)
        .setMediaId(md5)
        .build()

    override fun close() {
        if (isActive) cancel()
        MediaController.releaseFuture(controllerFuture)
    }

    data class PlayerSnapshot(
        val mediaId: String? = null,
        val status: PlayerStatusModel = PlayerStatusModel.IDLE,
        val repeatMode: PlayerRepeatMode = PlayerRepeatMode.OFF,
        val shuffleMode: PlayerShuffleMode = PlayerShuffleMode.OFF,
    )

    private fun PlayerRepeatMode.toSystemRepeatMode() = when (this) {
        PlayerRepeatMode.OFF -> Player.REPEAT_MODE_OFF
        PlayerRepeatMode.ON_SINGLE -> Player.REPEAT_MODE_ONE
        PlayerRepeatMode.ON_SESSION -> Player.REPEAT_MODE_ALL
    }

    private fun PlayerShuffleMode.toSystemShuffleMode() = when (this) {
        PlayerShuffleMode.OFF -> false
        PlayerShuffleMode.ON -> true
    }
}
