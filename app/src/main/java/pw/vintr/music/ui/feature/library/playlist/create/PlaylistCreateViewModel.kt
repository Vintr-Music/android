package pw.vintr.music.ui.feature.library.playlist.create

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.tools.validation.TextFieldValidator
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType

class PlaylistCreateViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(PlaylistCreateScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeName(value: String) {
        TextFieldValidator.validatePlaylistNameInput(value) { name ->
            _screenState.update { it.copy(name = name) }
        }
    }

    fun changeDescription(value: String) {
        TextFieldValidator.validatePlaylistDescriptionInput(value) { description ->
            _screenState.update { it.copy(description = description) }
        }
    }

    fun createPlaylist() {
        val freezeValue = _screenState.value

        launch(createExceptionHandler()) {
            withLoading(
                setLoadingCallback = { isLoading ->
                    _screenState.update { it.copy(isCreating = isLoading) }
                },
                action = {
                    playlistInteractor.createPlaylist(
                        name = freezeValue.name,
                        description = freezeValue.description
                    )
                    navigator.back(NavigatorType.Root)
                }
            )
        }
    }
}

data class PlaylistCreateScreenState(
    val name: String = String.Empty,
    val description: String = String.Empty,
    val isCreating: Boolean = false,
) {

    val formIsValid: Boolean = name.isNotEmpty()
}
