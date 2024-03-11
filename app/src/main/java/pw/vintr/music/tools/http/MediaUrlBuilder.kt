package pw.vintr.music.tools.http

import pw.vintr.music.BuildConfig
import java.net.URLEncoder

object MediaUrlBuilder {

    fun player(filePath: String) = BuildConfig.BASE_URL +
            "api/player/?key=${URLEncoder.encode(filePath, Charsets.UTF_8.name())}"

    fun artworkForTrack(filePath: String) = BuildConfig.BASE_URL +
            "api/library/artworks/track?key=${URLEncoder.encode(filePath, Charsets.UTF_8.name())}"

    fun artworkForAlbum(artist: String, album: String): String {
        val encodedArtistName = URLEncoder.encode(artist, Charsets.UTF_8.name())
        val encodedAlbumName = URLEncoder.encode(album, Charsets.UTF_8.name())

        return BuildConfig.BASE_URL +
                "api/library/artworks/album?artist=$encodedArtistName&album=$encodedAlbumName"
    }

    fun artworkForArtist(artist: String) = BuildConfig.BASE_URL +
            "api/library/artworks/artist?artist=$artist"

    fun artworkForPlaylist(id: String) = BuildConfig.BASE_URL +
            "api/library/artworks/playlist?id=$id"
}
