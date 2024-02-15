package pw.vintr.music.ui.feature.server.selection.connectNew

import pw.vintr.music.domain.server.model.ServerModel

data class ConnectNewServerResult(
    val connectedServer: ServerModel
) {
    companion object {
        const val KEY = "connect-new-server-result"
    }
}
