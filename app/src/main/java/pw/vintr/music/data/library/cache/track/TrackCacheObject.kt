package pw.vintr.music.data.library.cache.track

import androidx.annotation.Keep
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import pw.vintr.music.tools.extension.Empty

@Keep
class TrackCacheObject() : EmbeddedRealmObject {

    var md5: String = String.Empty

    var filePath: String = String.Empty

    var bitrate: Double = 0.0

    var codec: String? = null

    var codecProfile: String? = null

    var container: String? = null

    var duration: Double = 0.0

    var lossless: Boolean = false

    var numberOfChannels: Int = 0

    var sampleRate: Int = 0

    var tagTypes: RealmList<String> = realmListOf()

    var tool: String? = String.Empty

    var album: String? = null

    var artist: String? = null

    var artists: RealmList<String> = realmListOf()

    var genre: RealmList<String> = realmListOf()

    var title: String? = null

    var year: Int? = null

    var number: Int? = null

    constructor(
        md5: String = String.Empty,
        filePath: String = String.Empty,
        bitrate: Double = 0.0,
        codec: String? = null,
        codecProfile: String? = null,
        container: String? = null,
        duration: Double = 0.0,
        lossless: Boolean = false,
        numberOfChannels: Int = 0,
        sampleRate: Int = 0,
        tagTypes: RealmList<String> = realmListOf(),
        tool: String? = String.Empty,
        album: String? = null,
        artist: String? = null,
        artists: RealmList<String>,
        genre: RealmList<String> = realmListOf(),
        title: String? = null,
        year: Int? = null,
        number: Int? = null,
    ) : this() {
        this.md5 = md5
        this.filePath = filePath
        this.bitrate = bitrate
        this.codec = codec
        this.codecProfile = codecProfile
        this.container = container
        this.duration = duration
        this.lossless = lossless
        this.numberOfChannels = numberOfChannels
        this.sampleRate = sampleRate
        this.tagTypes = tagTypes
        this.tool = tool
        this.album = album
        this.artist = artist
        this.artists = artists
        this.genre = genre
        this.title = title
        this.year = year
        this.number = number
    }
}
