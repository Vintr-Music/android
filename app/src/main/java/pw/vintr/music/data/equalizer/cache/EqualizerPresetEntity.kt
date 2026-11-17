package pw.vintr.music.data.equalizer.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room row for a preset in the equalizer presets list.
 * [PresetCacheObject] stays a plain payload for domain / Gson / embedded currentPreset.
 */
@Entity(
    tableName = "equalizer_preset",
    foreignKeys = [
        ForeignKey(
            entity = EqualizerCacheObject::class,
            parentColumns = ["id"],
            childColumns = ["equalizerId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["equalizerId", "position"]),
    ],
)
data class EqualizerPresetEntity(
    @PrimaryKey(autoGenerate = true)
    val rowId: Long = 0,
    @ColumnInfo(index = true)
    val equalizerId: Int = EqualizerCacheObject.SINGLETON_ID,
    val position: Int = 0,
    val number: Short = 0,
    val name: String = "",
) {

    fun toCacheObject() = PresetCacheObject(number = number, name = name)

    companion object {
        fun from(
            preset: PresetCacheObject,
            position: Int,
            equalizerId: Int = EqualizerCacheObject.SINGLETON_ID,
        ) = EqualizerPresetEntity(
            equalizerId = equalizerId,
            position = position,
            number = preset.number,
            name = preset.name,
        )
    }
}
