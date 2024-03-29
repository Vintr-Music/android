package pw.vintr.music.app.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.datasource.DataSourceBitmapLoader.DEFAULT_EXECUTOR_SERVICE
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pw.vintr.music.R
import pw.vintr.music.app.main.MainActivity
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.domain.equalizer.interactor.EqualizerInteractor

class VintrMusicService : MediaSessionService(), KoinComponent {

    companion object {
        const val OPEN_APP_REQUEST_CODE = 10768
    }

    private var mediaSession: MediaSession? = null

    private val okHttpClient: OkHttpClient by inject()

    private val audioSessionRepository: AudioSessionRepository by inject()

    private val equalizerInteractor: EqualizerInteractor by inject()

    private val noisyAudioStreamReceiver = BecomingNoisyReceiver()

    private val noisyAudioIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    // If desired, validate the controller before returning the media session
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // Create your player and media session in the onCreate lifecycle event
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
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
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
