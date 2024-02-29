package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.EmbeddedRealmObject

@Keep
class BandCacheObject() : EmbeddedRealmObject {

    @SerializedName("number")
    var number: Short = 0

    @SerializedName("centerFrequency")
    var centerFrequency: Int = 0

    @SerializedName("lowerLevel")
    var lowerLevel: Int = 0

    @SerializedName("upperLevel")
    var upperLevel: Int = 0

    @SerializedName("currentLevel")
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
