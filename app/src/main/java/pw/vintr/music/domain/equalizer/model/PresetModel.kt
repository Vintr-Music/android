package pw.vintr.music.domain.equalizer.model

import pw.vintr.music.data.equalizer.cache.PresetCacheObject

data class PresetModel(
    val number: Short,
    val name: String,
) {
    fun toCacheObject() = PresetCacheObject(number, name)
}

fun PresetCacheObject.toModel() = PresetModel(number, name)
