package pw.vintr.music.ui.feature.server.accessControl.invite.details

import android.graphics.Bitmap
import com.google.zxing.Dimension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.domain.server.useCase.accessControl.GetServerInviteQRUseCase
import pw.vintr.music.ui.base.BaseViewModel

class ServerInviteDetailsViewModel(
    private val invite: ServerInviteModel,
    private val getServerInviteQRUseCase: GetServerInviteQRUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(ServerInviteDetailsState(invite))
    val screenState = _screenState.asStateFlow()

    fun loadQR(widthPx: Int, heightPx: Int) {
        launch(createExceptionHandler()) {
            _screenState.update {
                it.copy(
                    qrBitmap = getServerInviteQRUseCase.invoke(
                        inviteModel = invite,
                        dimension = Dimension(widthPx, heightPx)
                    )
                )
            }
        }
    }
}

data class ServerInviteDetailsState(
    val invite: ServerInviteModel,
    val qrBitmap: Bitmap? = null
)
