package pw.vintr.music.data.equalizer.cache

import androidx.room.Embedded
import androidx.room.Relation

data class EqualizerWithChildren(
    @Embedded
    val equalizer: EqualizerCacheObject,
    @Relation(
        parentColumn = "id",
        entityColumn = "equalizerId",
        entity = BandCacheObject::class,
    )
    val bands: List<BandCacheObject>,
    @Relation(
        parentColumn = "id",
        entityColumn = "equalizerId",
        entity = EqualizerPresetEntity::class,
    )
    val presets: List<EqualizerPresetEntity>,
) {

    fun toCacheObject(): EqualizerCacheObject =
        equalizer.withChildren(
            bands = bands.sortedBy { it.position },
            presets = presets.sortedBy { it.position }.map { it.toCacheObject() },
        )
}
