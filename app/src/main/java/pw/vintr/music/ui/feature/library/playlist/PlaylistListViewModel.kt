package pw.vintr.music.ui.feature.library.playlist

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.base.mapToScreenState
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class PlaylistListViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val screenState = playlistInteractor.dataFlow
        .mapLatest(::mapToScreenState)
        .stateInThis(BaseScreenState.Loading())

    init {
        loadData()
    }

    fun loadData() {
        launch { playlistInteractor.loadPlaylists() }
    }

    fun refreshData() {
        launch { playlistInteractor.refreshPlaylists() }
    }

    fun openCreatePlaylist() {
        navigator.forward(
            screen = Screen.PlaylistCreate,
            type = NavigatorType.Root
        )
    }

    fun openPlaylist(playlist: PlaylistModel) {
        // TODO: navigate
    }
}
