package pw.vintr.music.data.library.cache.album

import io.realm.kotlin.types.EmbeddedRealmObject
import pw.vintr.music.tools.extension.Empty

open class AlbumCacheObject() : EmbeddedRealmObject {

    var name: String? = null

    var artist: String = String.Empty

    var year: Int? = null

    constructor(
        name: String? = null,
        artist: String = String.Empty,
        year: Int? = null
    ) : this() {
        this.name = name
        this.artist = artist
        this.year = year
    }
}
