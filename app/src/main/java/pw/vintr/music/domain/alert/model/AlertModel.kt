package pw.vintr.music.domain.alert.model

import pw.vintr.music.R

sealed class AlertModel {

    abstract val titleRes: Int

    abstract val messageRes: Int

    data class CommonError(
        override val titleRes: Int = R.string.error_alert_title,
        override val messageRes: Int = R.string.error_alert_server_message
    ) : AlertModel()

    data class LoginError(
        override val titleRes: Int = R.string.error_alert_title,
        override val messageRes: Int = R.string.error_alert_login_message
    ) : AlertModel()
}
