package pw.vintr.music.domain.player.model.session

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import pw.vintr.music.app.constants.ConfigConstants
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel
import pw.vintr.music.tools.extension.urlEncode

sealed class PlayerSessionModel {

    companion object {
        protected const val QUEUE_TRACKS_LIMIT = 8000
    }

    abstract class Paged : PlayerSessionModel() {

        abstract val sessionStateKey: String

        abstract val totalCount: Int

        abstract val nextPageUrl: String

        abstract val nextPageParams: Map<String, Any>

        val canLoadNext: Boolean
            get() = tracks.size < QUEUE_TRACKS_LIMIT && tracks.size < totalCount

        val nextOffset: Int
            get() = tracks.size

        abstract fun insertNextPage(newPageTracks: List<TrackModel>): Paged
    }

    abstract val tracks: List<TrackModel>

    abstract fun toCacheObject(): PlayerSessionCacheObject

    abstract fun toCustomSession(): Custom

    fun isEmpty() = tracks.isEmpty()

    data object Empty : PlayerSessionModel() {
        override val tracks: List<TrackModel> = listOf()

        override fun toCacheObject() = PlayerSessionCacheObject(
            album = null,
            artist = null,
            tracks = realmListOf(),
        )

        override fun toCustomSession(): Custom = Custom(listOf())
    }

    data class Album(
        val album: AlbumModel,
        override val tracks: List<TrackModel>,
    ) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            album = album.toCacheObject(),
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = Custom(tracks)
    }

    data class Artist(
        val artist: ArtistModel,
        val sessionId: String,
        override val tracks: List<TrackModel>,
        override val totalCount: Int,
    ) : Paged() {

        override val sessionStateKey: String = sessionId + nextOffset.toString()

        override fun toCacheObject() = PlayerSessionCacheObject(
            artist = artist.name,
            sessionId = sessionId,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override val nextPageUrl: String = "api/library/tracks/shuffled"

        override val nextPageParams: Map<String, Any> = mapOf(
            "seed" to sessionId,
            "offset" to nextOffset,
            "limit" to ConfigConstants.TRACKS_PAGE_SIZE,
            "artist" to artist.name.urlEncode()
        )

        override fun toCustomSession() = Custom(tracks)

        override fun insertNextPage(newPageTracks: List<TrackModel>): Paged = copy(
            tracks = tracks + newPageTracks,
        )
    }

    data class Playlist(
        val playlistId: String,
        override val tracks: List<TrackModel>,
    ) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            playlistId = playlistId,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = Custom(tracks)
    }

    data class Flow(
        val sessionId: String,
        override val totalCount: Int,
        override val tracks: List<TrackModel>,
    ) : Paged() {

        override val sessionStateKey: String = sessionId + nextOffset.toString()

        override val nextPageUrl: String = "api/library/tracks/shuffled"

        override val nextPageParams: Map<String, Any> = mapOf(
            "seed" to sessionId,
            "offset" to nextOffset,
            "limit" to ConfigConstants.TRACKS_PAGE_SIZE
        )

        override fun toCacheObject() = PlayerSessionCacheObject(
            sessionId = sessionId,
            totalCount = totalCount,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = Custom(tracks)

        override fun insertNextPage(newPageTracks: List<TrackModel>): Paged = copy(
            tracks = tracks + newPageTracks,
        )
    }

    data class Custom(override val tracks: List<TrackModel>) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = this
    }
}

fun PlayerSessionCacheObject.toModel(): PlayerSessionModel {
    val tracks = tracks.map { it.toModel() }
    val album = album
    val artist = artist
    val playlistId = playlistId
    val flowSessionId = sessionId
    val totalCount = totalCount

    return when {
        album != null -> {
            PlayerSessionModel.Album(
                album = album.toModel(),
                tracks = tracks
            )
        }
        artist != null &&
        flowSessionId != null &&
        totalCount != null -> {
            PlayerSessionModel.Artist(
                artist = ArtistModel(artist),
                sessionId = flowSessionId,
                totalCount = totalCount,
                tracks = tracks,
            )
        }
        playlistId != null -> {
            PlayerSessionModel.Playlist(
                playlistId = playlistId,
                tracks = tracks
            )
        }
        flowSessionId != null && totalCount != null -> {
            PlayerSessionModel.Flow(
                sessionId = flowSessionId,
                totalCount = totalCount,
                tracks = tracks
            )
        }
        tracks.isNotEmpty() -> {
            PlayerSessionModel.Custom(
                tracks = tracks
            )
        }
        else -> {
            PlayerSessionModel.Empty
        }
    }
}
