package pw.vintr.music.data.library.cache.track

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.core.ui.tools.extension.Empty

/**
 * One queued track belonging to the singleton player session.
 * Scalar fields are real columns (not a JSON blob); string lists use [pw.vintr.music.data.cache.db.converter.StringListConverter].
 */
@Keep
@Entity(
    tableName = "player_session_track",
    foreignKeys = [
        ForeignKey(
            entity = PlayerSessionCacheObject::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["sessionId", "position"]),
    ],
)
data class TrackCacheObject(
    @PrimaryKey(autoGenerate = true)
    val rowId: Long = 0,
    @ColumnInfo(index = true)
    val sessionId: Int = PlayerSessionCacheObject.SINGLETON_ID,
    val position: Int = 0,
    val md5: String = String.Empty,
    val filePath: String = String.Empty,
    val bitrate: Double = 0.0,
    val codec: String? = null,
    val codecProfile: String? = null,
    val container: String? = null,
    val duration: Double = 0.0,
    val lossless: Boolean = false,
    val numberOfChannels: Int = 0,
    val sampleRate: Int = 0,
    val tagTypes: List<String> = emptyList(),
    val tool: String? = String.Empty,
    val album: String? = null,
    val artist: String? = null,
    val artists: List<String> = emptyList(),
    val genre: List<String> = emptyList(),
    val title: String? = null,
    val year: Int? = null,
    val number: Int? = null,
)
