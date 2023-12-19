package pw.vintr.music.ui.feature.trackDetails

import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class TrackDetailsViewModel : BaseViewModel() {

    fun openAlbum(trackModel: TrackModel) {
        navigator.closeNowPlaying()
        navigator.forward(
            Screen.AlbumDetails,
            Screen.AlbumDetails.arguments(trackModel.metadata.albumModel)
        )
        navigator.back(NavigatorType.Root)
    }

    fun openArtist(trackModel: TrackModel) {
        navigator.closeNowPlaying()
        navigator.forward(
            Screen.ArtistDetails,
            Screen.ArtistDetails.arguments(trackModel.metadata.artists.first())
        )
        navigator.back(NavigatorType.Root)
    }
}
