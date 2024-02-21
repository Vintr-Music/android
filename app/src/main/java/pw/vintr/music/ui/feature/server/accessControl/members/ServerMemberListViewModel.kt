package pw.vintr.music.ui.feature.server.accessControl.members

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.server.useCase.accessControl.GetServerMemberListUseCase
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

class ServerMemberListViewModel(
    private val serverId: String,
    private val getServerMemberListUseCase: GetServerMemberListUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<List<UserModel>>>(
        value = BaseScreenState.Loading()
    )
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling { getServerMemberListUseCase.invoke(serverId) }
    }

    fun refreshData() {
        _screenState.refreshWithStateHandling { getServerMemberListUseCase.invoke(serverId) }
    }

    fun openMemberDetails(member: UserModel) {
        // TODO: open
    }
}
