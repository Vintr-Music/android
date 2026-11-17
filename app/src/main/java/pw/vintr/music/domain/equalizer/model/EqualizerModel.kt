package pw.vintr.music.domain.equalizer.model

import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject

data class EqualizerModel(
    val bands: List<BandModel>,
    val presets: List<PresetModel>,
    val currentPreset: PresetModel? = null,
    val enabled: Boolean = false,
) {
    fun toCacheObject() = EqualizerCacheObject(
        bands = bands.map { it.toCacheObject() },
        presets = presets.map { it.toCacheObject() },
        currentPreset = currentPreset?.toCacheObject(),
        enabled = enabled,
    )
}

fun EqualizerCacheObject.toModel() = EqualizerModel(
    bands = bands.map { it.toModel() },
    presets = presets.map { it.toModel() },
    currentPreset = currentPreset?.toModel(),
    enabled = enabled,
)
