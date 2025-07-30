package pw.vintr.music.ui.feature.artistTracks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pw.vintr.music.app.constants.ConfigConstants.TRACKS_PAGE_SIZE
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetShuffledTracksPageUseCase
import pw.vintr.music.domain.pagination.controller.PaginationController
import pw.vintr.music.domain.pagination.model.PageInteractionState
import pw.vintr.music.domain.pagination.model.withFirstPageLoading
import pw.vintr.music.domain.pagination.model.withNextPageLoading
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class ArtistTracksViewModel(
    private val artist: ArtistModel,
    private val playingSessionId: String,
    private val getShuffledTracksPageUseCase: GetShuffledTracksPageUseCase,
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    private val tracksPagingController = PaginationController(
        limit = TRACKS_PAGE_SIZE
    ) { limit, offset ->
        getShuffledTracksPageUseCase(
            artist = artist.name,
            sessionId = playingSessionId,
            offset = offset,
            limit = limit
        )
    }

    private val _pageInteractionState = MutableStateFlow<PageInteractionState>(
        value = PageInteractionState.Loading
    )

    val screenState = combine(
        _pageInteractionState,
        playerInteractor.playerState,
        tracksPagingController.pagingState,
    ) { pageInteractionState, playerState, pagingState ->
        when (pageInteractionState) {
            is PageInteractionState.Loading -> {
                BaseScreenState.Loading()
            }
            is PageInteractionState.Error -> {
                BaseScreenState.Error()
            }
            is PageInteractionState.Succeed -> {
                BaseScreenState.Loaded(
                    data = ArtistTracksScreenData(
                        artist = artist,
                        tracks = pagingState.items,
                        totalCount = pagingState.totalCount,
                        playingState = if (
                            playerState.session is PlayerSessionModel.Artist &&
                            playerState.session.artist.name == artist.name
                        ) {
                            ArtistTracksScreenData.PlayingState(
                                playingTrack = playerState.currentTrack,
                                playerStatus = playerState.status
                            )
                        } else {
                            ArtistTracksScreenData.PlayingState(
                                playingTrack = playerState.currentTrack
                            )
                        },
                        hasNextPage = pagingState.hasNext,
                        hasFailedPage = pageInteractionState.hasFailedPage,
                        loadingNextPage = pageInteractionState.loadingNextPage,
                    )
                )
            }
        }
    }.stateInThis(BaseScreenState.Loading())

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        launch(createExceptionHandler()) {
            _pageInteractionState.withFirstPageLoading {
                tracksPagingController.loadFirstPage()
            }
        }
    }

    fun loadNextPage() {
        launch(createExceptionHandler()) {
            _pageInteractionState.withNextPageLoading {
                tracksPagingController.loadNextPage()
            }
        }
    }

    fun playArtist(startIndex: Int = 0) {
        launch {
            screenState.withLoaded {
                playerInteractor.playArtist(
                    artist = artist,
                    startIndex = startIndex,
                    tracks = it.tracks,
                    totalCount = it.totalCount,
                    sessionId = playingSessionId,
                )
            }
        }
    }

    fun pauseArtist() {
        playerInteractor.pause()
    }

    fun resumeArtist() {
        playerInteractor.resume()
    }

    fun openTrackAction(track: TrackModel) {
        navigator.forward(
            Screen.TrackActionSheet(
                trackModel = track,
                allowedActions = TrackAction.actionsExceptAlbumNavigate
            ),
            NavigatorType.Root
        )
    }
}

data class ArtistTracksScreenData(
    val artist: ArtistModel,
    val tracks: List<TrackModel>,
    val totalCount: Int,
    val playingState: PlayingState,
    val hasNextPage: Boolean,
    val hasFailedPage: Boolean,
    val loadingNextPage: Boolean,
) {
    val title = artist.name

    data class PlayingState(
        val playingTrack: TrackModel? = null,
        val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE
    )
}
