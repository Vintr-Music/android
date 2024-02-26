package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

@Keep
class EqualizerCacheObject() : RealmObject {

    var bands: RealmList<BandCacheObject> = realmListOf()

    var presets: RealmList<PresetCacheObject> = realmListOf()

    var currentPreset: PresetCacheObject? = null

    var enabled: Boolean = false

    constructor(
        bands: RealmList<BandCacheObject> = realmListOf(),
        presets: RealmList<PresetCacheObject> = realmListOf(),
        currentPreset: PresetCacheObject? = null,
        enabled: Boolean = false
    ) : this() {
        this.bands = bands
        this.presets = presets
        this.currentPreset = currentPreset
        this.enabled = enabled
    }
}
