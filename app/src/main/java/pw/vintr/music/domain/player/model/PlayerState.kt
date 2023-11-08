package pw.vintr.music.domain.player.model

sealed interface PlayerState {

    val trackId: String?

    object Idle : PlayerState {
        override val trackId: String? = null
    }

    data class Paused(
        override val trackId: String
    ) : PlayerState

    data class Playing(
        override val trackId: String
    ) : PlayerState
}
