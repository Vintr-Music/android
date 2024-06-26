package pw.vintr.music.data.player.cache

import androidx.annotation.Keep
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import pw.vintr.music.data.library.cache.album.AlbumCacheObject
import pw.vintr.music.data.library.cache.track.TrackCacheObject

@Keep
class PlayerSessionCacheObject() : RealmObject {

    var album: AlbumCacheObject? = null

    var artist: String? = null

    var playlistId: String? = null

    var tracks: RealmList<TrackCacheObject> = realmListOf()

    constructor(
        album: AlbumCacheObject? = null,
        artist: String? = null,
        playlistId: String? = null,
        tracks: RealmList<TrackCacheObject> = realmListOf()
    ) : this() {
        this.album = album
        this.artist = artist
        this.playlistId = playlistId
        this.tracks = tracks
    }
}
