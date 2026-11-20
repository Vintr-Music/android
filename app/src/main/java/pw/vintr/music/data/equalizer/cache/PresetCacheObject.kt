package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import pw.vintr.music.core.ui.tools.extension.Empty

/**
 * Preset payload for domain / Gson / @Embedded currentPreset.
 * Queued presets are persisted as [EqualizerPresetEntity] rows.
 */
@Keep
data class PresetCacheObject(
    @SerializedName("number")
    val number: Short = 0,
    @SerializedName("name")
    val name: String = String.Empty,
)
