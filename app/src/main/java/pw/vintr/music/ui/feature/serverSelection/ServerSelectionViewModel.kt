package pw.vintr.music.ui.feature.serverSelection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.useCase.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.SelectServerUseCase
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ServerSelectionViewModel(
    private val getServerListUseCase: GetServerListUseCase,
    private val selectServerUseCase: SelectServerUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<ServerSelectionScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        launch(createExceptionHandler {
            _screenState.value = BaseScreenState.Error()
        }) {
            _screenState.value = BaseScreenState.Loading()
            _screenState.value = BaseScreenState.Loaded(
                ServerSelectionScreenData(
                    servers = getServerListUseCase.invoke(),
                )
            )
        }
    }

    fun selectServer(server: ServerModel) {
        _screenState.updateLoaded { it.copy(selection = server) }
    }

    fun confirmSelection() {
        _screenState.withLoaded { state ->
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

data class ServerSelectionScreenData(
    val servers: List<ServerModel>,
    val selection: ServerModel? = null,
) {
    val formIsValid = selection != null
}
