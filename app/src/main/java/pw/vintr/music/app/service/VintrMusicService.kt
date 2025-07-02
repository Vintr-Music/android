package pw.vintr.music.app.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.datasource.DataSourceBitmapLoader.DEFAULT_EXECUTOR_SERVICE
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pw.vintr.music.R
import pw.vintr.music.app.main.MainActivity
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.data.library.repository.TrackRepository
import pw.vintr.music.data.player.repository.PlayerSessionRepository
import pw.vintr.music.domain.equalizer.interactor.EqualizerInteractor
import pw.vintr.music.domain.library.model.track.toMediaItem
import pw.vintr.music.domain.library.model.track.toModel
import pw.vintr.music.domain.pagination.model.toModel
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.session.toModel

class VintrMusicService : MediaSessionService(), KoinComponent {

    companion object {
        const val OPEN_APP_REQUEST_CODE = 10768
        private const val TRACKS_LOAD_THRESHOLD = 3
    }

    private var mediaSession: MediaSession? = null

    private var isLoadingNextPage = false
    private var loadNextPageJob: Job? = null

    private val okHttpClient: OkHttpClient by inject()

    private val equalizerInteractor: EqualizerInteractor by inject()

    private val trackRepository: TrackRepository by inject()
    private val audioSessionRepository: AudioSessionRepository by inject()
    private val playerSessionRepository: PlayerSessionRepository by inject()

    private val noisyAudioStreamReceiver = BecomingNoisyReceiver()
    private val noisyAudioIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    // If desired, validate the controller before returning the media session
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // Create player and media session in the onCreate lifecycle event
    @UnstableApi
    override fun onCreate() {
        super.onCreate()

        // Notification init
        val notificationProvider = DefaultMediaNotificationProvider
            .Builder(applicationContext)
            .build()

        notificationProvider.setSmallIcon(R.drawable.ic_media_notification)
        setMediaNotificationProvider(notificationProvider)

        // Player init
        val dataSource = OkHttpDataSource.Factory(okHttpClient)

        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSource))
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true,
            )
            .build()

        // Media session init
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    OPEN_APP_REQUEST_CODE,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setBitmapLoader(
                DataSourceBitmapLoader(
                    DEFAULT_EXECUTOR_SERVICE.get(),
                    dataSource
                )
            )
            .build()

        audioSessionRepository.setSessionId(player.audioSessionId)
        equalizerInteractor.initAsync(player.audioSessionId)

        // Noisy audio detection
        registerReceiver(noisyAudioStreamReceiver, noisyAudioIntentFilter)

        // Setup player listener for paging tracks
        setupPlayerListener()
    }

    private fun setupPlayerListener() {
        mediaSession?.player?.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                checkAndLoadNextPageIfNeeded()
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    checkAndLoadNextPageIfNeeded()
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                checkAndLoadNextPageIfNeeded()
            }
        })
    }

    private fun checkAndLoadNextPageIfNeeded() {
        val player = mediaSession?.player ?: return

        // Don't load if already loading or if there's no current media item
        if (isLoadingNextPage || player.currentMediaItem == null) return

        val remainingTracks = calculateRemainingTracks(player)
        if (remainingTracks <= TRACKS_LOAD_THRESHOLD) {
            loadNextPageJob?.cancel()
            loadNextPageJob = CoroutineScope(Dispatchers.IO).launch {
                isLoadingNextPage = true

                try {
                    loadNextPage()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoadingNextPage = false
                }
            }
        }
    }

    private fun calculateRemainingTracks(player: Player): Int {
        return player.mediaItemCount - player.currentMediaItemIndex - 1
    }

    private suspend fun loadNextPage() {
        val sessionToLoad = getCurrentSession()

        if (
            sessionToLoad is PlayerSessionModel.Paged &&
            sessionToLoad.canLoadNext
        ) {
            val nextPageTracks = trackRepository.getTracksPage(
                urlString = sessionToLoad.nextPageUrl,
                params = sessionToLoad.nextPageParams,
            ).toModel { it.toModel() }

            val actualSession = getCurrentSession()
            if (
                actualSession is PlayerSessionModel.Paged &&
                actualSession.sessionStateKey == sessionToLoad.sessionStateKey
            ) {
                withContext(Dispatchers.Main) {
                    // Update session
                    val updatedActualSession = actualSession
                        .insertNextPage(nextPageTracks.data)
                    playerSessionRepository
                        .savePlayerSession(updatedActualSession.toCacheObject())

                    // Insert tracks in Media3 queue
                    mediaSession?.player
                        ?.addMediaItems(nextPageTracks.data.map { it.toMediaItem() })
                }
            }
        }
    }

    private suspend fun getCurrentSession() = playerSessionRepository
        .getPlayerSession()
        ?.toModel()

    override fun onDestroy() {
        loadNextPageJob?.cancel()
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        unregisterReceiver(noisyAudioStreamReceiver)
        super.onDestroy()
    }

    inner class BecomingNoisyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                mediaSession?.player?.pause()
            }
        }
    }
}
