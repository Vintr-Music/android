package pw.vintr.music.ui.feature.nowPlaying.manageSession

import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.ui.base.BaseViewModel

class ManageSessionViewModel(
    private val playerInteractor: PlayerInteractor
) : BaseViewModel() {

    val screenData = playerInteractor.playerState
        .stateInThis(initialValue = PlayerStateHolderModel())

    fun seekToTrack(trackIndex: Int) {
        playerInteractor.seekToTrack(trackIndex)
    }
}
