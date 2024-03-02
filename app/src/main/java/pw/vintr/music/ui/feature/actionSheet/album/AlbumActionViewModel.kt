package pw.vintr.music.ui.feature.actionSheet.album

import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumAction
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionResult
import pw.vintr.music.ui.navigation.NavigatorType

class AlbumActionViewModel : BaseViewModel() {

    fun playNext() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = AlbumActionResult.KEY,
            result = AlbumActionResult(AlbumAction.PLAY_NEXT)
        )
    }

    fun addToQueue() {
        navigator.back(
            type = NavigatorType.Root,
            resultKey = AlbumActionResult.KEY,
            result = AlbumActionResult(AlbumAction.ADD_TO_QUEUE)
        )
    }
}
