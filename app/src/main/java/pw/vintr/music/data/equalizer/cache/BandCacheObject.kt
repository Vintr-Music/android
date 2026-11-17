package pw.vintr.music.data.equalizer.cache

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * One equalizer band belonging to the singleton equalizer document.
 */
@Keep
@Entity(
    tableName = "equalizer_band",
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
data class BandCacheObject(
    @PrimaryKey(autoGenerate = true)
    val rowId: Long = 0,
    @ColumnInfo(index = true)
    val equalizerId: Int = EqualizerCacheObject.SINGLETON_ID,
    val position: Int = 0,
    @SerializedName("number")
    val number: Short = 0,
    @SerializedName("centerFrequency")
    val centerFrequency: Int = 0,
    @SerializedName("lowerLevel")
    val lowerLevel: Int = 0,
    @SerializedName("upperLevel")
    val upperLevel: Int = 0,
    @SerializedName("currentLevel")
    val currentLevel: Int = 0,
)
