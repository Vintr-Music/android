package pw.vintr.music.ui.feature.nowPlaying.manageSession

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.tools.extension.reorder
import pw.vintr.music.ui.base.BaseViewModel
import java.util.UUID

class ManageSessionViewModel(
    private val playerInteractor: PlayerInteractor
) : BaseViewModel() {

    private val playerState = playerInteractor.playerState

    val screenState = MutableStateFlow(ScreenState())

    init {
        listenPlayerState()
    }

    private fun listenPlayerState() {
        launch {
            playerState.collectLatest { state ->
                val currentState = screenState.value
                val listsEquals = state.session.tracks == currentState.tracks.map { it.track }

                screenState.value = currentState.copy(
                    tracks = if (listsEquals) {
                        currentState.tracks
                    } else {
                        state.session.tracks.map { TrackWrapper(it) }
                    },
                    currentTrack = state.currentTrack,
                )
            }
        }
    }

    fun reorder(from: Int, to: Int) {
        screenState.value.reorderState?.let { reorder ->
            val tracks = reorder.reorderedTracks.reorder(from, to)

            screenState.update {
                it.copy(
                    reorderState = reorder.copy(
                        reorderedTracks = tracks,
                        endIndex = to,
                    )
                )
            }
        } ?: run {
            val tracks = screenState.value.tracks.reorder(from, to)

            screenState.update {
                it.copy(
                    reorderState = ReorderState(
                        reorderedTracks = tracks,
                        startIndex = from,
                        endIndex = to,
                    )
                )
            }
        }
    }

    fun saveReorder() {
        screenState.value.reorderState?.let { reorder ->
            launch {
                screenState.update { state ->
                    state.copy(
                        tracks = reorder.reorderedTracks,
                        reorderState = null,
                    )
                }
                playerInteractor.reorder(reorder.startIndex, reorder.endIndex)
            }
        }
    }

    fun seekToTrack(trackIndex: Int) {
        playerInteractor.seekToTrack(trackIndex)
    }
}

data class TrackWrapper(
    val track: TrackModel,
    val listUUID: String = UUID.randomUUID().toString(),
)

data class ReorderState(
    val reorderedTracks: List<TrackWrapper> = listOf(),
    val startIndex: Int,
    val endIndex: Int,
)

data class ScreenState(
    val tracks: List<TrackWrapper> = listOf(),
    val currentTrack: TrackModel? = null,
    val reorderState: ReorderState? = null,
) {
    val uiTracks get() = reorderState?.reorderedTracks ?: tracks
}
