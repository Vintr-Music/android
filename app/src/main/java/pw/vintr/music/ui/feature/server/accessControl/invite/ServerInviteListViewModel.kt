package pw.vintr.music.ui.feature.server.accessControl.invite

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.domain.server.useCase.accessControl.GetServerInviteListUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ServerInviteListViewModel(
    private val serverId: String,
    private val getServerInviteListUseCase: GetServerInviteListUseCase,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<List<ServerInviteModel>>>(
        value = BaseScreenState.Loading()
    )
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling { getServerInviteListUseCase.invoke(serverId) }
    }

    fun refreshData() {
        _screenState.refreshWithStateHandling { getServerInviteListUseCase.invoke(serverId) }
    }

    fun openInviteDetails(inviteModel: ServerInviteModel) {
        navigator.forward(Screen.ServerInviteDetails(inviteModel))
    }
}
