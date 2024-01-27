package pw.vintr.music.ui.feature.serverSelection.connectNewServer

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pw.vintr.music.R
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel

class ConnectNewServerViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(ConnectNewServerScreenState())

    val screenState = _screenState.asStateFlow()

    fun selectTab(tabType: ConnectNewServerTabType) {
        val newTabData = when (tabType) {
            ConnectNewServerTabType.QR -> ConnectNewServerTabData.QRTab()
            ConnectNewServerTabType.MANUAl -> ConnectNewServerTabData.ManualTab()
        }

        _screenState.update { it.copy(tabData = newTabData) }
    }
}

enum class ConnectNewServerTabType(
    @StringRes
    val tabTitleRes: Int
) {
    QR(tabTitleRes = R.string.server_add_by_qr),
    MANUAl(tabTitleRes = R.string.server_add_by_manual);

    companion object {
        val records = listOf(QR, MANUAl)
    }
}

sealed interface ConnectNewServerTabData {

    val tabType: ConnectNewServerTabType

    data class QRTab(
        val qrData: String = String.Empty,
    ) : ConnectNewServerTabData {
        override val tabType = ConnectNewServerTabType.QR
    }

    data class ManualTab(
        val serverName: String = String.Empty,
        val inviteCode: String = String.Empty,
    ) : ConnectNewServerTabData {
        override val tabType = ConnectNewServerTabType.MANUAl
    }
}

data class ConnectNewServerScreenState(
    val isConnectingServer: Boolean = false,
    val tabData: ConnectNewServerTabData = ConnectNewServerTabData.QRTab()
)
