package pw.vintr.music.ui.feature.library.playlist.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.domain.playlist.model.PlaylistRecordModel
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.updateWithLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistAction
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionResult
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackActionResult
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogTemplate.openDeletePlaylistConfirmDialog
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogTemplate.openDeleteTrackConfirmDialog
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class PlaylistDetailsViewModel(
    private val playlistId: String,
    private val playerInteractor: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<PlaylistDetailsScreenData>>(
        value = BaseScreenState.Loading()
    )

    val playlistPlayingState = playerInteractor.playerState.map {
        if (
            it.session is PlayerSessionModel.Playlist &&
            it.session.playlistId == playlistId
        ) {
            PlaylistPlayingState(
                playingTrack = it.currentTrack,
                playerStatus = it.status
            )
        } else {
            PlaylistPlayingState(it.currentTrack)
        }
    }.stateInThis(PlaylistPlayingState())

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
        subscribePlaylistEvents()
    }

    fun loadData() {
        _screenState.loadWithStateHandling(
            emptyCheckAction = { it.records.isEmpty() }
        ) {
            val tracks = async {
                playlistInteractor.getPlaylistTracks(playlistId)
            }
            val playlist = async {
                playlistInteractor.getPlaylistById(playlistId)
            }

            PlaylistDetailsScreenData(
                playlist = playlist.await(),
                records = tracks.await(),
            )
        }
    }

    private fun subscribePlaylistEvents() {
        launch {
            playlistInteractor.events
                .filter { it.playlistId == playlistId }
                .collectLatest(::processPlaylistEvent)
        }
    }

    private fun processPlaylistEvent(event: PlaylistInteractor.Event) {
        when (event) {
            is PlaylistInteractor.Event.AddedTrack -> {
                onTrackAdded(event.record)
            }
            is PlaylistInteractor.Event.RemovedTrack -> {
                onTrackRemoved(event.record)
            }
            is PlaylistInteractor.Event.UpdatedTracks -> {
                onTracksUpdated(event.records)
            }
        }
    }

    private fun onTrackAdded(record: PlaylistRecordModel) {
        _screenState.updateLoaded(onDifferentType = { loadData() }) { freezeData ->
            freezeData.copy(records = listOf(record, *freezeData.records.toTypedArray()))
        }
    }

    private fun onTrackRemoved(record: PlaylistRecordModel) {
        _screenState.updateWithLoaded(onDifferentType = { loadData() }) { freezeData ->
            val newRecords = freezeData.records
                .filter { it != record }

            if (newRecords.isNotEmpty()) {
                BaseScreenState.Loaded(freezeData.copy(records = newRecords))
            } else {
                BaseScreenState.Empty()
            }
        }
    }

    private fun onTracksUpdated(records: List<PlaylistRecordModel>) {
        _screenState.updateLoaded(onDifferentType = { loadData() }) { freezeData ->
            freezeData.copy(records = records)
        }
    }

    fun playPlaylist(startIndex: Int = 0) {
        launch {
            _screenState.withLoaded { data ->
                playerInteractor.playPlaylist(
                    tracks = data.records.map { it.track },
                    playlist = data.playlist,
                    startIndex = startIndex
                )
            }
        }
    }

    fun pausePlaylist() {
        playerInteractor.pause()
    }

    fun resumePlaylist() {
        playerInteractor.resume()
    }

    fun openTrackAction(record: PlaylistRecordModel) {
        handleResult(TrackActionResult.KEY) {
            navigator.forwardWithResult<TrackActionResult>(
                screen = Screen.TrackActionSheet(
                    trackModel = record.track,
                    allowedActions = listOf(
                        TrackAction.GO_TO_ALBUM,
                        TrackAction.GO_TO_ARTIST,
                        TrackAction.PLAY_NEXT,
                        TrackAction.ADD_TO_QUEUE,
                        TrackAction.DELETE_FROM_PLAYLIST,
                    )
                ),
                type = NavigatorType.Root,
                resultKey = TrackActionResult.KEY
            ) { result ->
                if (result.action == TrackAction.DELETE_FROM_PLAYLIST) {
                    navigator.openDeleteTrackConfirmDialog {
                        deleteTrackFromPlaylist(record)
                    }
                }
            }
        }
    }

    private fun deleteTrackFromPlaylist(record: PlaylistRecordModel) {
        launch(createExceptionHandler()) {
            withPrimaryLoader { playlistInteractor.removeTrackFromPlaylist(record) }
        }
    }

    fun openPlaylistAction() {
        _screenState.withLoaded { screenData ->
            val screen = Screen.PlaylistActionSheet(
                playlistActionSheetInfo = PlaylistActionSheetInfo(
                    playlist = screenData.playlist,
                    tracksCount = screenData.records.size,
                    playDurationMillis = screenData.records
                        .sumOf { it.track.format.duration }
                        .toLong()
                )
            )

            handleResult(PlaylistActionResult.KEY) {
                navigator.forwardWithResult<PlaylistActionResult>(
                    screen,
                    NavigatorType.Root,
                    PlaylistActionResult.KEY
                ) {
                    handlePlaylistAction(
                        action = it.action,
                        tracks = screenData.records.map { record -> record.track }
                    )
                }
            }
        }
    }

    private fun handlePlaylistAction(
        action: PlaylistAction,
        tracks: List<TrackModel>
    ) {
        when (action) {
            PlaylistAction.PLAY_NEXT -> {
                launch { playerInteractor.setPlayNext(tracks) }
            }
            PlaylistAction.ADD_TO_QUEUE -> {
                launch { playerInteractor.addToQueue(tracks) }
            }
            PlaylistAction.EDIT_PLAYLIST -> {
                navigator.forward(
                    screen = Screen.PlaylistEdit(playlistId),
                    type = NavigatorType.Root
                )
            }
            PlaylistAction.DELETE_PLAYLIST -> {
                navigator.openDeletePlaylistConfirmDialog { deletePlaylist() }
            }
        }
    }

    private fun deletePlaylist() {
        launch(context = Dispatchers.Main + createExceptionHandler()) {
            withPrimaryLoader { playlistInteractor.removePlaylist(playlistId) }
            navigator.back()
        }
    }
}

data class PlaylistDetailsScreenData(
    val playlist: PlaylistModel,
    val records: List<PlaylistRecordModel>,
) {
    val title = playlist.name

    val subtitle = playlist.description
}

data class PlaylistPlayingState(
    val playingTrack: TrackModel? = null,
    val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE
)
