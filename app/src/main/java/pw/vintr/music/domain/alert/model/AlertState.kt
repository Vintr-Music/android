package pw.vintr.music.domain.alert.model

data class AlertState(
    val alert: AlertModel? = null,
    val show: Boolean = false,
) {
    val alertVisible = alert != null && show
}
