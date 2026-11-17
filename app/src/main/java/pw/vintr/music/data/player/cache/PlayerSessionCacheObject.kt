package pw.vintr.music.data.player.cache

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pw.vintr.music.data.library.cache.album.AlbumCacheObject
import pw.vintr.music.data.library.cache.track.TrackCacheObject

/**
 * Singleton player-session document.
 * Tracks live in `player_session_track` and are attached via @Relation.
 */
@Keep
@Entity(tableName = "player_session")
data class PlayerSessionCacheObject(
    @PrimaryKey
    val id: Int = SINGLETON_ID,
    @Embedded(prefix = "album_")
    val album: AlbumCacheObject? = null,
    val artist: String? = null,
    val playlistId: String? = null,
    val sessionId: String? = null,
    val totalCount: Int? = null,
) {

    @Ignore
    var tracks: List<TrackCacheObject> = emptyList()

    /** Convenience for domain mappers that still pass tracks at construction time. */
    constructor(
        album: AlbumCacheObject? = null,
        artist: String? = null,
        playlistId: String? = null,
        sessionId: String? = null,
        totalCount: Int? = null,
        tracks: List<TrackCacheObject> = emptyList(),
    ) : this(
        id = SINGLETON_ID,
        album = album,
        artist = artist,
        playlistId = playlistId,
        sessionId = sessionId,
        totalCount = totalCount,
    ) {
        this.tracks = tracks
    }

    fun withTracks(tracks: List<TrackCacheObject>): PlayerSessionCacheObject =
        copy().also { it.tracks = tracks }

    companion object {
        const val SINGLETON_ID = 0
    }
}
