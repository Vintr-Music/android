package pw.vintr.music.domain.equalizer.model

data class BandModel(
    val number: Short,
    val centerFrequency: Int,
    val lowerLevel: Int,
    val upperLevel: Int,
    val currentLevel: Int,
)
