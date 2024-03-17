package pw.vintr.music.ui.feature.library.playlist.edit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.playlist.model.PlaylistModel
import pw.vintr.music.domain.playlist.model.PlaylistRecordModel
import pw.vintr.music.tools.extension.reorder
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.tools.validation.TextFieldValidator
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType

class PlaylistEditViewModel(
    private val playlistId: String,
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<PlaylistEditScreenData>>(
        value = BaseScreenState.Loading()
    )
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
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

            PlaylistEditScreenData(
                savedPlaylist = playlist.await(),
                name = playlist.await().name,
                description = playlist.await().description,
                records = tracks.await(),
            )
        }
    }

    fun changeName(value: String) {
        TextFieldValidator.validatePlaylistNameInput(value) { name ->
            _screenState.updateLoaded { it.copy(name = name) }
        }
    }

    fun changeDescription(value: String) {
        TextFieldValidator.validatePlaylistDescriptionInput(value) { description ->
            _screenState.updateLoaded { it.copy(description = description) }
        }
    }

    fun reorder(from: Int, to: Int) {
        _screenState.updateLoaded {
            it.copy(
                records = it.records.reorder(from, to),
                isRecordsModified = true
            )
        }
    }

    fun savePlaylist() {
        _screenState.withLoaded { freezeData ->
            launch(context = Dispatchers.Main + createExceptionHandler()) {
                withPrimaryLoader {
                    val updateInfoRequest = if (freezeData.canSaveInfo) {
                        async { updatePlaylistInfo(freezeData) }
                    } else { null }

                    val updateTracksRequest = if (freezeData.isRecordsModified) {
                        async { updatePlaylistTracks(freezeData) }
                    } else { null }

                    updateInfoRequest?.await()
                    updateTracksRequest?.await()
                }

                navigator.back(NavigatorType.Root)
            }
        }
    }

    private suspend fun updatePlaylistInfo(freezeData: PlaylistEditScreenData) {
        playlistInteractor.updatePlaylist(
            playlistId = playlistId,
            name = freezeData.name,
            description = freezeData.description
        )
    }

    private suspend fun updatePlaylistTracks(freezeData: PlaylistEditScreenData) {
        playlistInteractor.updateTracks(
            playlistId = playlistId,
            newRecords = freezeData.records
                .reversed()
                .mapIndexed { index, record -> record.copy(ordinal = index) }
        )
    }
}

data class PlaylistEditScreenData(
    val savedPlaylist: PlaylistModel,
    val name: String,
    val description: String,
    val records: List<PlaylistRecordModel>,
    val isRecordsModified: Boolean = false,
) {
    val canSaveInfo = name.isNotEmpty() &&
            (name != savedPlaylist.name || description != savedPlaylist.description)

    val canBeSaved: Boolean = canSaveInfo || isRecordsModified
}
