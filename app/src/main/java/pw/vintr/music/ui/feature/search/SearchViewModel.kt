package pw.vintr.music.ui.feature.search

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.search.SearchContent
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.SearchLibraryUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.state.PlayerStateHolderModel
import pw.vintr.music.domain.search.SearchHistoryInteractor
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class SearchViewModel(
    private val searchLibraryUseCase: SearchLibraryUseCase,
    private val playerInteractor: PlayerInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : BaseViewModel() {

    private val _queryState = MutableStateFlow(String.Empty)
    private val _contentState = MutableStateFlow<BaseScreenState<SearchContent>>(
        value = BaseScreenState.Empty()
    )

    val queryState = _queryState.asStateFlow()
    val playerState = playerInteractor.playerState.stateInThis(PlayerStateHolderModel())

    val screenState = combine(
        _contentState,
        searchHistoryInteractor.getSearchQueryFlow()
    ) { content, history ->
        when (content) {
            is BaseScreenState.Error<SearchContent>,
            is BaseScreenState.Loaded<SearchContent>,
            is BaseScreenState.Loading<SearchContent> -> {
                content
            }
            is BaseScreenState.Empty -> {
                if (history.isNotEmpty()) {
                    SearchContentState.QueryHistory(history)
                } else {
                    content
                }
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }.stateInThis(initialValue = BaseScreenState.Loading())

    private var searchJob: Job? = null

    fun changeQuery(value: String) {
        _queryState.value = value
    }

    fun performSearch() {
        searchJob?.cancel()

        val query = _queryState.value.trim()

        if (query.isNotEmpty()) {
            searchJob = _contentState.loadWithStateHandling {
                searchHistoryInteractor.saveQuery(query)
                searchLibraryUseCase.invoke(query)
            }
        } else {
            _contentState.value = BaseScreenState.Empty()
        }
    }

    fun clearSearch() {
        searchJob?.cancel()

        _queryState.value = String.Empty
        _contentState.value = BaseScreenState.Empty()
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

    fun openTrackAction(track: TrackModel) {
        navigator.forward(
            Screen.TrackActionSheet(
                trackModel = track,
                allowedActions = listOf(
                    TrackAction.GO_TO_ALBUM,
                    TrackAction.GO_TO_ARTIST,
                    TrackAction.PLAY_NEXT,
                    TrackAction.ADD_TO_QUEUE,
                ),
            ),
            NavigatorType.Root
        )
    }
}

sealed interface SearchContentState : BaseScreenState<SearchContent> {
    data class QueryHistory(val queries: List<String>) : SearchContentState
}
