package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

/**
 * Singleton equalizer document.
 * Bands / presets live in child tables; [currentPreset] is @Embedded.
 */
@Keep
@Entity(tableName = "equalizer")
data class EqualizerCacheObject(
    @PrimaryKey
    val id: Int = SINGLETON_ID,
    @SerializedName("enabled")
    val enabled: Boolean = false,
    @Embedded(prefix = "current_preset_")
    @SerializedName("currentPreset")
    val currentPreset: PresetCacheObject? = null,
) {

    @Ignore
    @SerializedName("bands")
    var bands: List<BandCacheObject> = emptyList()

    @Ignore
    @SerializedName("presets")
    var presets: List<PresetCacheObject> = emptyList()

    constructor(
        bands: List<BandCacheObject> = emptyList(),
        presets: List<PresetCacheObject> = emptyList(),
        currentPreset: PresetCacheObject? = null,
        enabled: Boolean = false,
    ) : this(
        id = SINGLETON_ID,
        enabled = enabled,
        currentPreset = currentPreset,
    ) {
        this.bands = bands
        this.presets = presets
    }

    fun withChildren(
        bands: List<BandCacheObject>,
        presets: List<PresetCacheObject>,
    ): EqualizerCacheObject = copy().also {
        it.bands = bands
        it.presets = presets
    }

    companion object {
        const val SINGLETON_ID = 0
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
            context: JsonDeserializationContext?,
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
                bands = bands,
                presets = presets,
                currentPreset = currentPreset,
                enabled = enabled,
            )
        }
    }
}
