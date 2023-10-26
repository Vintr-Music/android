package pw.vintr.music.ui.feature.serverSelection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.useCase.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.SelectServerUseCase
import pw.vintr.music.tools.extension.updateTyped
import pw.vintr.music.tools.extension.withTyped
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ServerSelectionViewModel(
    private val getServerListUseCase: GetServerListUseCase,
    private val selectServerUseCase: SelectServerUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<ServerSelectionState>(ServerSelectionState.Loading)

    val screenState = _screenState.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        launch(createExceptionHandler {
            _screenState.value = ServerSelectionState.Error
        }) {
            _screenState.value = ServerSelectionState.Loaded(
                servers = getServerListUseCase.invoke(),
            )
        }
    }

    fun selectServer(server: ServerModel) {
        _screenState.updateTyped<ServerSelectionState, ServerSelectionState.Loaded> {
            it.copy(selection = server)
        }
    }

    fun confirmSelection() {
        _screenState.withTyped<ServerSelectionState.Loaded> { state ->
            state.selection?.let { server ->
                selectServerUseCase.invoke(server)
                navigator.replaceAll(Screen.Root)
            }
        }
    }

    fun openAddNewServer() {
        // TODO: navigate to add server
    }
}

sealed interface ServerSelectionState {

    object Loading : ServerSelectionState

    object Error : ServerSelectionState

    data class Loaded(
        val servers: List<ServerModel>,
        val selection: ServerModel? = null,
    ) : ServerSelectionState {
        val formIsValid = selection != null
    }
}
