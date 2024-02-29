package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import java.lang.reflect.Type

@Keep
class EqualizerCacheObject() : RealmObject {

    @SerializedName("bands")
    var bands: RealmList<BandCacheObject> = realmListOf()

    @SerializedName("presets")
    var presets: RealmList<PresetCacheObject> = realmListOf()

    @SerializedName("currentPreset")
    var currentPreset: PresetCacheObject? = null

    @SerializedName("enabled")
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

    class Deserializer : JsonDeserializer<EqualizerCacheObject> {

        companion object {
            private const val BANDS_FIELD_KEY = "bands"
            private const val PRESETS_FIELD_KEY = "presets"
            private const val CURRENT_PRESET_FIELD_KEY = "currentPreset"
            private const val ENABLED_FIELD_KEY = "enabled"
        }

        class DeserializeException : Exception()

        private val gson = Gson()

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): EqualizerCacheObject {
            val bands = json?.asJsonObject?.getAsJsonArray(BANDS_FIELD_KEY)?.map {
                gson.fromJson(it, BandCacheObject::class.java)
            } ?: throw DeserializeException()

            val presets = json.asJsonObject?.getAsJsonArray(PRESETS_FIELD_KEY)?.map {
                gson.fromJson(it, PresetCacheObject::class.java)
            } ?: throw DeserializeException()

            val currentPreset = json.asJsonObject?.getAsJsonObject(
                CURRENT_PRESET_FIELD_KEY
            )?.let {
                gson.fromJson(it, PresetCacheObject::class.java)
            }

            val enabled = json.asJsonObject?.get(ENABLED_FIELD_KEY)?.asBoolean ?: false

            return EqualizerCacheObject(
                bands = bands.toRealmList(),
                presets = presets.toRealmList(),
                currentPreset = currentPreset,
                enabled = enabled,
            )
        }
    }
}
