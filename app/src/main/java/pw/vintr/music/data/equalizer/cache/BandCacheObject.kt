package pw.vintr.music.data.equalizer.cache

import io.realm.kotlin.types.EmbeddedRealmObject

class BandCacheObject() : EmbeddedRealmObject {

    var number: Short = 0

    var centerFrequency: Int = 0

    var lowerLevel: Int = 0

    var upperLevel: Int = 0

    var currentLevel: Int = 0

    constructor(
        number: Short = 0,
        centerFrequency: Int = 0,
        lowerLevel: Int = 0,
        upperLevel: Int = 0,
        currentLevel: Int = 0
    ) : this() {
        this.number = number
        this.centerFrequency = centerFrequency
        this.lowerLevel = lowerLevel
        this.upperLevel = upperLevel
        this.currentLevel = currentLevel
    }
}
