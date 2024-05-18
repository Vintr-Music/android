package pw.vintr.music.tools.extension

import androidx.media3.common.C
import androidx.media3.common.Player

val Player.currentUnshuffledIndex: Int
    get() {
        val timeline = currentTimeline

        return if (timeline.isEmpty) {
            C.INDEX_UNSET
        } else {
            timeline.getNextWindowIndex(
                currentMediaItemIndex,
                Player.REPEAT_MODE_OFF,
                false
            )
        }
    }
