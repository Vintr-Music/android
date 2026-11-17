package pw.vintr.music.data.player.cache

import androidx.room.Embedded
import androidx.room.Relation
import pw.vintr.music.data.library.cache.track.TrackCacheObject

data class PlayerSessionWithTracks(
    @Embedded
    val session: PlayerSessionCacheObject,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId",
        entity = TrackCacheObject::class,
    )
    val tracks: List<TrackCacheObject>,
) {

    fun toCacheObject(): PlayerSessionCacheObject =
        session.withTracks(tracks.sortedBy { it.position })
}
