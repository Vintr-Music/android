package pw.vintr.music.ui.feature.albumDetails

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.favorite.FavoriteAlbumsInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumAction
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionResult
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class AlbumDetailsViewModel(
    private val album: AlbumModel,
    private val playerInteractor: PlayerInteractor,
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase,
    private val favoriteAlbumsInteractor: FavoriteAlbumsInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<AlbumDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val albumPlayingState = playerInteractor.playerState.map {
        if (
            it.session is PlayerSessionModel.Album &&
            it.session.album == album
        ) {
            AlbumPlayingState(
                playingTrack = it.currentTrack,
                playerStatus = it.status
            )
        } else {
            AlbumPlayingState(it.currentTrack)
        }
    }.stateInThis(AlbumPlayingState())

    val screenState = _screenState.asStateFlow()

    private var favoritesInteractionJob: Job? = null

    init {
        loadData()
        subscribeFavoriteEvents()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val tracks = async {
                getAlbumTracksUseCase.invoke(
                    artist = album.artist.name,
                    album = album.name
                )
            }
            val isFavorite = async {
                favoriteAlbumsInteractor.isInFavorites(album)
            }

            AlbumDetailsScreenData(
                album = album,
                tracks = tracks.await(),
                isFavorite = isFavorite.await()
            )
        }
    }

    private fun subscribeFavoriteEvents() {
        launch {
            favoriteAlbumsInteractor.events
                .filter { it.album == album }
                .collectLatest { event ->
                    when (event) {
                        is FavoriteAlbumsInteractor.Event.Added -> {
                            _screenState.updateLoaded { it.copy(isFavorite = true) }
                        }

                        is FavoriteAlbumsInteractor.Event.Removed -> {
                            _screenState.updateLoaded { it.copy(isFavorite = false) }
                        }
                    }
            }
        }
    }

    fun playAlbum(startIndex: Int = 0) {
        launch {
            _screenState.withLoaded {
                playerInteractor.playAlbum(it.tracks, album, startIndex)
            }
        }
    }

    fun pauseAlbum() {
        playerInteractor.pause()
    }

    fun resumeAlbum() {
        playerInteractor.resume()
    }

    fun addToFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteAlbumsInteractor.addToFavorites(album)
            }
        }
    }

    fun removeFromFavorites() {
        if (favoritesInteractionJob?.isActive != true) {
            favoritesInteractionJob = launch(createExceptionHandler()) {
                favoriteAlbumsInteractor.removeFromFavorites(album)
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

    fun openAlbumAction() {
        _screenState.withLoaded { screenData ->
            val screen = Screen.AlbumActionSheet(
                albumActionSheetInfo = AlbumActionSheetInfo(
                    album = screenData.album,
                    tracksCount = screenData.tracks.size,
                    playDurationMillis = screenData.tracks
                        .sumOf { it.format.duration }
                        .toLong()
                )
            )

            handleResult(AlbumActionResult.KEY) {
                navigator.forwardWithResult<AlbumActionResult>(
                    screen,
                    NavigatorType.Root,
                    AlbumActionResult.KEY
                ) { handleAlbumAction(it.action, screenData.tracks) }
            }
        }
    }

    private fun handleAlbumAction(action: AlbumAction, tracks: List<TrackModel>) {
        when (action) {
            AlbumAction.PLAY_NEXT -> {
                launch { playerInteractor.setPlayNext(tracks) }
            }
            AlbumAction.ADD_TO_QUEUE -> {
                launch { playerInteractor.addToQueue(tracks) }
            }
        }
    }
}

data class AlbumDetailsScreenData(
    val album: AlbumModel,
    val tracks: List<TrackModel>,
    val isFavorite: Boolean,
) {
    val title = album.name

    val subtitle = album.artistAndYear
}

data class AlbumPlayingState(
    val playingTrack: TrackModel? = null,
    val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE
)
