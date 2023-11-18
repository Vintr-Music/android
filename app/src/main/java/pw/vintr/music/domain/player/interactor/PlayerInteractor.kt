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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import pw.vintr.music.app.service.VintrMusicService
import pw.vintr.music.data.player.repository.PlayerSessionRepository
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.PlayerSessionModel
import pw.vintr.music.domain.player.model.PlayerStateHolderModel
import pw.vintr.music.domain.player.model.PlayerStatusModel
import pw.vintr.music.domain.player.model.toModel
import java.io.Closeable

class PlayerInteractor(
    applicationContext: Context,
    private val playerSessionRepository: PlayerSessionRepository,
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

    private val playerSnapshotFlow = MutableStateFlow(PlayerSnapshot())

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
        )
    }.shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    init {
        controllerFuture.addListener({
            controller = controllerFuture.get()

            controller?.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    onPlayerEvent(player)
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun onPlayerEvent(player: Player) {
        val mediaId = player.currentMediaItem?.mediaId

        playerSnapshotFlow.value = when {
            mediaId != null && player.isPlaying -> {
                PlayerSnapshot(mediaId, PlayerStatusModel.PLAYING)
            }
            mediaId != null -> {
                PlayerSnapshot(mediaId, PlayerStatusModel.PAUSED)
            }
            else -> {
                PlayerSnapshot(status = PlayerStatusModel.IDLE)
            }
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
    )
}
