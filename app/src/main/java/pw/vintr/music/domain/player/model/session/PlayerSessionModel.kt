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

    abstract val tracks: List<TrackModel>

    abstract fun toCacheObject(): PlayerSessionCacheObject

    abstract fun toCustomSession(): Custom

    fun isEmpty() = tracks.isEmpty()

    object Empty : PlayerSessionModel() {
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
