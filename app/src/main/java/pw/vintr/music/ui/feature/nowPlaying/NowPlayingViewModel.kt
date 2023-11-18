package pw.vintr.music.ui.feature.nowPlaying

import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.PlayerStateHolderModel
import pw.vintr.music.ui.base.BaseViewModel

class NowPlayingViewModel(
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    val playerStateFlow = playerInteractor.playerState
        .stateInThis(PlayerStateHolderModel())

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
}
