package pw.vintr.music.domain.player.interactor

import android.content.ComponentName
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pw.vintr.music.app.service.VintrMusicService
import pw.vintr.music.data.player.repository.PlayerConfigRepository
import pw.vintr.music.data.player.repository.PlayerSessionRepository
import pw.vintr.music.data.settings.repository.SettingsRepository
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.model.config.PlayerRepeatMode
import pw.vintr.music.domain.player.model.config.PlayerShuffleMode
import pw.vintr.music.domain.player.model.state.PlayerProgressModel
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.player.model.session.toModel
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.tools.extension.hasNoItems
import pw.vintr.music.tools.extension.reorder

class PlayerInteractor(
    applicationContext: Context,
    private val playerSessionRepository: PlayerSessionRepository,
    private val playerConfigRepository: PlayerConfigRepository,
    private val settingsRepository: SettingsRepository,
) : BaseInteractor() {

    private var controller: MediaController? = null

    private val audioManager = applicationContext.getSystemService(
        Context.AUDIO_SERVICE
    ) as AudioManager

    private val sessionToken = SessionToken(
        applicationContext,
        ComponentName(applicationContext, VintrMusicService::class.java)
    )

    private val controllerFuture = MediaController
        .Builder(applicationContext, sessionToken)
        .buildAsync()

    private val playerSnapshotFlow = MutableStateFlow(
        PlayerSnapshot(
            repeatMode = PlayerRepeatMode.getByCode(playerConfigRepository.getRepeatMode()),
            shuffleMode = PlayerShuffleMode.getByCode(playerConfigRepository.getShuffleMode())
        )
    )

    private val _askPlaySpeakersEvent = Channel<suspend () -> Unit>()
    val askPlaySpeakersEvent by lazy { _askPlaySpeakersEvent.receiveAsFlow() }

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

            controller?.addListener(@UnstableApi object : Player.Listener {
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
        playSession(
            session = PlayerSessionModel.Album(
                album = album,
                tracks = tracks
            ),
            startIndex = startIndex
        )
    }

    suspend fun playPlaylist(
        tracks: List<TrackModel>,
        playlist: PlaylistModel,
        startIndex: Int = 0,
    ) {
        playSession(
            session = PlayerSessionModel.Playlist(
                playlistId = playlist.id,
                tracks = tracks
            ),
            startIndex = startIndex
        )
    }

    suspend fun playQueue(tracks: List<TrackModel>, startIndex: Int = 0) {
        playSession(
            session = PlayerSessionModel.Custom(tracks),
            startIndex = startIndex
        )
    }

    private suspend fun playSession(
        session: PlayerSessionModel,
        startIndex: Int
    ) {
        withAskPlaySpeakers {
            controller?.stop()
            playerSessionRepository.savePlayerSession(session.toCacheObject())

            controller?.setMediaItems(
                session.tracks.map { it.toMediaItem() },
                startIndex,
                0
            )
            controller?.play()
        }
    }

    suspend fun setPlayNext(tracks: List<TrackModel>) {
        playerSessionRepository.getPlayerSession()?.let { session ->
            val hasNoMediaItems = controller.hasNoItems()
            val currentSessionModel = session.toModel()

            val currentPlayingIndex = session.tracks
                .indexOfFirst { it.md5 == controller?.currentMediaItem?.mediaId }
                .takeIf { it != -1 } ?: 0
            val newItemsStartIndex = currentPlayingIndex + 1

            val modifiedSession = currentSessionModel
                .toCustomSession()
                .let { customSession ->
                    val newTrackList = if (hasNoMediaItems || customSession.isEmpty()) {
                        tracks
                    } else {
                        customSession.tracks
                            .toMutableList()
                            .apply { addAll(newItemsStartIndex, tracks) }
                    }

                    customSession.copy(tracks = newTrackList)
                }

            playerSessionRepository.savePlayerSession(session = modifiedSession.toCacheObject())
            controller?.addMediaItems(newItemsStartIndex, tracks.map { it.toMediaItem() })

            if (hasNoMediaItems) { resume() }
        }
    }

    suspend fun addToQueue(tracks: List<TrackModel>) {
        playerSessionRepository.getPlayerSession()?.let { session ->
            val hasNoMediaItems = controller.hasNoItems()
            val currentSessionModel = session.toModel()
            val modifiedSession = currentSessionModel
                .toCustomSession()
                .let { customSession ->
                    val newTrackList = if (hasNoMediaItems || customSession.isEmpty()) {
                        tracks
                    } else {
                        customSession.tracks + tracks
                    }

                    customSession.copy(tracks = newTrackList)
                }

            playerSessionRepository.savePlayerSession(session = modifiedSession.toCacheObject())
            controller?.addMediaItems(tracks.map { it.toMediaItem() })

            if (hasNoMediaItems) { resume() }
        }
    }

    suspend fun reorder(fromIndex: Int, toIndex: Int) {
        playerSessionRepository.getPlayerSession()?.let { session ->
            val hasNoMediaItems = controller.hasNoItems()
            val currentSessionModel = session.toModel()
            val modifiedSession = currentSessionModel
                .toCustomSession()
                .let { customSession ->
                    val newTrackList = if (hasNoMediaItems || customSession.isEmpty()) {
                        listOf()
                    } else {
                        customSession.tracks.reorder(fromIndex, toIndex)
                    }

                    customSession.copy(tracks = newTrackList)
                }

            playerSessionRepository.savePlayerSession(session = modifiedSession.toCacheObject())
            controller?.moveMediaItem(fromIndex, toIndex)
        }
    }

    fun pause() {
        controller?.pause()
    }

    fun resume() {
        launch {
            withAskPlaySpeakers {
                controller?.play()
            }
        }
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

    fun seekToTrack(trackIndex: Int, autoPlay: Boolean = true) {
        controller?.seekTo(trackIndex, 0L)

        if (controller?.isPlaying != true && autoPlay) {
            controller?.play()
        }
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

    suspend fun destroySession() {
        controller?.stop()
        controller?.clearMediaItems()

        playerSessionRepository.removePlayerSession()
    }

    override fun close() {
        super.close()
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

    private fun TrackModel.toMediaItem() = MediaItem.Builder()
        .setUri(playerUrl)
        .setMediaId(md5)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setArtist(metadata.artist)
                .setTitle(metadata.title)
                .setAlbumTitle(metadata.album)
                .setArtworkUri(
                    runCatching { Uri.parse(artworkUrl) }
                        .getOrNull()
                )
                .build()
        )
        .build()

    private suspend fun withAskPlaySpeakers(action: suspend () -> Unit) {
        if (controller?.isPlaying != true && needSpeakersWarning()) {
            _askPlaySpeakersEvent.send(action)
        } else {
            action()
        }
    }

    private fun needSpeakersWarning(): Boolean {
        audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).forEach { device ->
            if (
                device.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                device.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                device.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
                device.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
            ) {
                return false
            }
        }

        return settingsRepository.getNeedSpeakerNotification()
    }
}
