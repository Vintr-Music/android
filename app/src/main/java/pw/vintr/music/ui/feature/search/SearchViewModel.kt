package pw.vintr.music.ui.feature.search

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.search.SearchContent
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.SearchLibraryUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class SearchViewModel(
    private val searchLibraryUseCase: SearchLibraryUseCase,
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    private val _queryState = MutableStateFlow(String.Empty)
    private val _contentState = MutableStateFlow<BaseScreenState<SearchContent>>(
        value = SearchContentState.Empty
    )

    val queryState = _queryState.asStateFlow()
    val contentState = _contentState.asStateFlow()
    val playerState = playerInteractor.playerState.stateInThis(PlayerStateHolderModel())

    private var searchJob: Job? = null

    fun changeQuery(value: String) {
        _queryState.value = value
    }

    fun performSearch() {
        searchJob?.cancel()

        val query = _queryState.value.trim()

        if (query.isNotEmpty()) {
            searchJob = _contentState.loadWithStateHandling {
                searchLibraryUseCase.invoke(query)
            }
        } else {
            _contentState.value = SearchContentState.Empty
        }
    }

    fun clearSearch() {
        searchJob?.cancel()

        _queryState.value = String.Empty
        _contentState.value = SearchContentState.Empty
    }

    fun onArtistClick(artist: ArtistModel) {
        navigator.forward(Screen.ArtistDetails(artist))
    }

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }

    fun onTrackClicked(tracks: List<TrackModel>, track: TrackModel) {
        launch {
            playerInteractor.playQueue(
                tracks = tracks,
                startIndex = tracks.indexOf(track)
            )
        }
    }

    fun openTrackDetails(track: TrackModel) {
        navigator.forward(
            Screen.TrackDetails(track),
            NavigatorType.Root
        )
    }
}

sealed interface SearchContentState : BaseScreenState<SearchContent> {
    object Empty : SearchContentState
}
