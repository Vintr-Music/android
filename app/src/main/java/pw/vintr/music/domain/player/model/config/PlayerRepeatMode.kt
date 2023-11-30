package pw.vintr.music.domain.player.model.config

enum class PlayerRepeatMode(val code: Int) {
    OFF(code = 0),
    ON_SINGLE(code = 1),
    ON_SESSION(code = 2);

    companion object {
        fun getByCode(code: Int) = values().find { it.code == code } ?: OFF
    }
}
