package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import io.realm.kotlin.types.EmbeddedRealmObject
import pw.vintr.music.tools.extension.Empty

@Keep
class PresetCacheObject() : EmbeddedRealmObject {

    var number: Short = 0

    var name: String = String.Empty

    constructor(
        number: Short = 0,
        name: String = String.Empty,
    ) : this() {
        this.number = number
        this.name = name
    }
}
