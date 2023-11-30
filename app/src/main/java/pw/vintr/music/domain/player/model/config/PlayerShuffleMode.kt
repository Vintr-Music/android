package pw.vintr.music.domain.player.model.config

enum class PlayerShuffleMode(val code: Int) {
    OFF(code = 0),
    ON(code = 1);

    companion object {
        fun getByCode(code: Int) = values().find { it.code == code } ?: OFF
    }
}
