package pw.vintr.music.domain.player.model

data class PlayerProgressModel(
    val progress: Float = 0f,
    val duration: Float = 0f,
    val mediaId: String? = null,
    val isLoading: Boolean = false
) {
    val isOnStart: Boolean = progress == 0f
}
