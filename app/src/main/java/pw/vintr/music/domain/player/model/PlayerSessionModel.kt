package pw.vintr.music.domain.player.model

import io.realm.kotlin.ext.copyFromRealm
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

    object Empty : PlayerSessionModel() {
        override val tracks: List<TrackModel> = listOf()

        override fun toCacheObject() = PlayerSessionCacheObject(
            album = null,
            artist = null,
            tracks = realmListOf(),
        )
    }

    data class Album(
        val album: AlbumModel,
        override val tracks: List<TrackModel>,
    ) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            album = album.toCacheObject(),
            artist = null,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )
    }

    data class Artist(
        val artist: ArtistModel,
        override val tracks: List<TrackModel>,
    ) : PlayerSessionModel() {

        override fun toCacheObject() = PlayerSessionCacheObject(
            album = null,
            artist = artist.name,
            tracks = tracks
                .map { it.toCacheObject() }
                .toRealmList()
        )
    }
}

fun PlayerSessionCacheObject.toModel(): PlayerSessionModel {
    val tracks = tracks.map { it.copyFromRealm().toModel() }
    val album = album?.copyFromRealm()
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
        else -> {
            PlayerSessionModel.Empty
        }
    }
}
