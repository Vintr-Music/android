package pw.vintr.music.app.service

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VintrMusicService : MediaSessionService(), KoinComponent {

    private var mediaSession: MediaSession? = null

    private val okHttpClient: OkHttpClient by inject()

    // If desired, validate the controller before returning the media session
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // Create your player and media session in the onCreate lifecycle event
    @UnstableApi
    override fun  onCreate() {
        super.onCreate()

        val dataSource = OkHttpDataSource.Factory(okHttpClient)
        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSource))
            .build()

        mediaSession = MediaSession.Builder(this, player).build()
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
