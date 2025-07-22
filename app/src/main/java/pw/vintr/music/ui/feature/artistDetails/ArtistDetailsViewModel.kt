package pw.vintr.music.ui.feature.artistDetails

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.favorite.FavoriteArtistsInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.domain.library.useCase.GetShuffledTracksPageUseCase
import pw.vintr.music.domain.pagination.model.PageModel
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen
import java.util.UUID

class ArtistDetailsViewModel(
    private val artist: ArtistModel,
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
    private val getShuffledTracksPageUseCase: GetShuffledTracksPageUseCase,
    private val favoriteArtistsInteractor: FavoriteArtistsInteractor,
    private val playerInteractor: PlayerInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<ArtistDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val artistPlayingState = playerInteractor.playerState.map {
        if (
            it.session is PlayerSessionModel.Artist &&
            it.session.artist.name == artist.name
        ) {
            ArtistPlayingState(
                playingTrack = it.currentTrack,
                playerStatus = it.status
            )
        } else {
            ArtistPlayingState(it.currentTrack)
        }
    }.stateInThis(ArtistPlayingState())

    val screenState = _screenState.asStateFlow()

    private var favoritesInteractionJob: Job? = null

    init {
        loadData()
        subscribeFavoriteEvents()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val artistSessionId = async {
                val currentSession = playerInteractor.getCurrentSession()
                if (
                    currentSession is PlayerSessionModel.Artist &&
                    currentSession.artist == artist
                ) {
                    currentSession.sessionId
                } else {
                    UUID.randomUUID().toString()
                }
            }
            val tracksPage = async {
                getShuffledTracksPageUseCase(
                    artist = artist.name,
                    sessionId = artistSessionId.await(),
                )
            }
            val albums = async {
                getArtistAlbumsUseCase.invoke(artist.name)
            }
            val isFavorite = async {
                favoriteArtistsInteractor.isInFavorites(artist)
            }

            ArtistDetailsScreenData(
                artist = artist,
                albums = albums.await(),
                isFavorite = isFavorite.await(),
                tracksPage = tracksPage.await(),
                sessionId = artistSessionId.await()
            )
        }
    }

    private fun subscribeFavoriteEvents() {
        launch {
            favoriteArtistsInteractor.events
                .filter { it.artist == artist }
                .collectLatest { event ->
                    when (event) {
                        is FavoriteArtistsInteractor.Event.Added -> {
                            _screenState.updateLoaded { it.copy(isFavorite = true) }
                        }

                        is FavoriteArtistsInteractor.Event.Removed -> {
                            _screenState.updateLoaded { it.copy(isFavorite = false) }
                        }
                    }
                }
        }
    }

    fun playArtist(startIndex: Int = 0) {
        launch {
            _screenState.withLoaded {
                playerInteractor.playArtist(
                    artist = artist,
                    startIndex = startIndex,
                    tracks = it.tracksPage.data,
                    totalCount = it.tracksPage.count,
                    sessionId = it.sessionId,
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

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }

    fun addToFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteArtistsInteractor.addToFavorites(artist)
            }
        }
    }

    fun removeFromFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteArtistsInteractor.removeFromFavorites(artist)
            }
        }
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

    fun openAllTracks() {
        _screenState.withLoaded {
            navigator.forward(Screen.ArtistTracks(artist, it.sessionId))
        }
    }
}

data class ArtistDetailsScreenData(
    val artist: ArtistModel,
    val albums: List<AlbumModel>,
    val isFavorite: Boolean,
    val tracksPage: PageModel<TrackModel>,
    val sessionId: String,
) {
    val title = artist.name
}

data class ArtistPlayingState(
    val playingTrack: TrackModel? = null,
    val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE
)
