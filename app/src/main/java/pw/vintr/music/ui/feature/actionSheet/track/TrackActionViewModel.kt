package pw.vintr.music.ui.feature.actionSheet.track

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class TrackActionViewModel(
    private val playerInteractor: PlayerInteractor
) : BaseViewModel() {

    fun openAlbum(trackModel: TrackModel) {
        navigator.closeNowPlaying()
        navigator.forward(Screen.AlbumDetails(trackModel.metadata.albumModel))
        navigator.back(NavigatorType.Root)
    }

    fun openArtist(trackModel: TrackModel) {
        navigator.closeNowPlaying()
        navigator.forward(Screen.ArtistDetails(trackModel.metadata.artists.first()))
        navigator.back(NavigatorType.Root)
    }

    fun playNext(trackModel: TrackModel) {
        launch(Dispatchers.Main) {
            playerInteractor.setPlayNext(listOf(trackModel))
            navigator.back(NavigatorType.Root)
        }
    }

    fun addToQueue(trackModel: TrackModel) {
        launch(Dispatchers.Main) {
            playerInteractor.addToQueue(listOf(trackModel))
            navigator.back(NavigatorType.Root)
        }
    }
}
