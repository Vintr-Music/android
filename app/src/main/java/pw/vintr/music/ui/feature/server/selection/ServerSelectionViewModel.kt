package pw.vintr.music.ui.feature.server.selection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.domain.server.useCase.selection.GetSelectedServerUseCase
import pw.vintr.music.domain.server.useCase.list.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.selection.SelectServerUseCase
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.navigation.Screen

class ServerSelectionViewModel(
    private val getServerListUseCase: GetServerListUseCase,
    private val getSelectedServerUseCase: GetSelectedServerUseCase,
    private val selectServerUseCase: SelectServerUseCase,
    private val playerInteractor: PlayerInteractor
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<ServerSelectionScreenData>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling {
            val selectedServer = runCatching { getSelectedServerUseCase.invoke() }
                .getOrNull()

            ServerSelectionScreenData(
                servers = getServerListUseCase.invoke(),
                selection = selectedServer,
                savedSelection = selectedServer
            )
        }
    }

    fun selectServer(server: ServerModel) {
        _screenState.updateLoaded { it.copy(selection = server) }
    }

    fun confirmSelection() {
        launch(Dispatchers.Main) {
            _screenState.withLoaded { state ->
                state.selection?.let { server ->
                    playerInteractor.destroySession()
                    selectServerUseCase.invoke(server)
                    navigator.replaceAll(Screen.Root, type = NavigatorType.Root)
                }
            }
        }
    }

    fun openAddNewServer() {
        navigator.forward(Screen.ConnectNewServer)
    }
}

data class ServerSelectionScreenData(
    val servers: List<ServerModel>,
    val selection: ServerModel? = null,
    val savedSelection: ServerModel? = null,
) {
    val formIsValid = selection != savedSelection
}
