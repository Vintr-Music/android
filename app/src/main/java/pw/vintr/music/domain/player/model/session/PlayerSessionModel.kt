package pw.vintr.music.domain.player.model.session

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel

sealed class PlayerSessionModel {

    companion object {
        protected const val QUEUE_TRACKS_LIMIT = 8000
    }

    abstract class Paged : PlayerSessionModel() {

        abstract val totalCount: Int

        val canLoadNext: Boolean
            get() = tracks.size < QUEUE_TRACKS_LIMIT && tracks.size < totalCount

        val nextOffset: Int
            get() = tracks.size
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
        override val tracks: List<TrackModel>,
    ) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            artist = artist.name,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = Custom(tracks)
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
        val flowSessionId: String,
        override val totalCount: Int,
        override val tracks: List<TrackModel>,
    ) : Paged() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            flowSessionId = flowSessionId,
            totalCount = totalCount,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )

        override fun toCustomSession() = Custom(tracks)
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
    val flowSessionId = flowSessionId
    val totalCount = totalCount

    return when {
        album != null -> {
            PlayerSessionModel.Album(
                album = album.toModel(),
                tracks = tracks
            )
        }
        artist != null -> {
            PlayerSessionModel.Artist(
                artist = ArtistModel(artist),
                tracks = tracks
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
                flowSessionId = flowSessionId,
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
