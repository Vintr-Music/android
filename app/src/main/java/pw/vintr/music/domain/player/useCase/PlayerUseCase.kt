package pw.vintr.music.domain.player.useCase

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import pw.vintr.music.app.service.VintrMusicService
import pw.vintr.music.domain.library.model.track.TrackModel

class PlayerUseCase(
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

    init {
        controllerFuture.addListener({
            controller = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    fun invokePlay(tracks: List<TrackModel>) {
        val track = tracks.first()
        val url = track.playerUrl

        Log.e("TEST", url)

        controller?.addMediaItem(MediaItem.fromUri(url))
        controller?.play()
    }
}
