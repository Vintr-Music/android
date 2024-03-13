package pw.vintr.music.ui.feature.actionSheet.playlist

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistAction
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionResult
import pw.vintr.music.ui.navigation.NavigatorType

class PlaylistActionViewModel : BaseViewModel() {

    fun playNext() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = PlaylistActionResult.KEY,
            result = PlaylistActionResult(PlaylistAction.PLAY_NEXT)
        )
    }

    fun addToQueue() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = PlaylistActionResult.KEY,
            result = PlaylistActionResult(PlaylistAction.ADD_TO_QUEUE)
        )
    }

    fun editPlaylist() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = PlaylistActionResult.KEY,
            result = PlaylistActionResult(PlaylistAction.ADD_TO_QUEUE)
        )
    }

    fun deletePlaylist() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = PlaylistActionResult.KEY,
            result = PlaylistActionResult(PlaylistAction.DELETE_PLAYLIST)
        )
    }
}
