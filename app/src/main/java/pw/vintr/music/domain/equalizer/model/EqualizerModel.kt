package pw.vintr.music.domain.equalizer.model

data class EqualizerModel(
    val bands: List<BandModel>,
    val presets: List<PresetModel>,
    val currentPreset: PresetModel? = null,
    val enabled: Boolean = false,
)
