package pw.vintr.music.domain.equalizer.model

import pw.vintr.music.data.equalizer.cache.BandCacheObject

data class BandModel(
    val number: Short,
    val centerFrequency: Int,
    val lowerLevel: Int,
    val upperLevel: Int,
    val currentLevel: Int,
) {
    fun toCacheObject() = BandCacheObject(
        number,
        centerFrequency,
        lowerLevel,
        upperLevel,
        currentLevel
    )
}

fun BandCacheObject.toModel() = BandModel(
    number,
    centerFrequency,
    lowerLevel,
    upperLevel,
    currentLevel
)
