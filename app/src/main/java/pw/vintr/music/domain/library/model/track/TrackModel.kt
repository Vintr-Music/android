package pw.vintr.music.domain.library.model.track

import android.net.Uri
import android.os.Parcelable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import io.realm.kotlin.ext.toRealmList
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pw.vintr.music.data.library.cache.track.TrackCacheObject
import pw.vintr.music.data.library.dto.track.TrackDto
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.tools.http.MediaUrlBuilder

@Parcelize
data class TrackModel(
    val filePath: String,
    val md5: String,
    val format: TrackFormatModel,
    val metadata: TrackMetadataModel,
) : Parcelable {

    @IgnoredOnParcel
    val playerUrl = MediaUrlBuilder.player(filePath)

    @IgnoredOnParcel
    val artworkUrl = MediaUrlBuilder.artworkForTrack(filePath)

    fun toCacheObject() = TrackCacheObject(
        md5 = md5,
        filePath = filePath,
        bitrate = format.bitrate,
        codec = format.codec,
        codecProfile = format.codecProfile,
        container = format.container,
        duration = format.duration,
        lossless = format.lossless,
        numberOfChannels = format.numberOfChannels,
        sampleRate = format.sampleRate,
        tagTypes = format.tagTypes.toRealmList(),
        tool = format.tool,
        album = metadata.album,
        artists = metadata.artists
            .map { it.name }
            .toRealmList(),
        genre = metadata.genre.toRealmList(),
        title = metadata.title,
        number = metadata.number,
        year = metadata.year,
    )
}

fun TrackDto.toModel() = TrackModel(
    filePath = filePath,
    format = format.toModel(),
    md5 = md5,
    metadata = metadata.toModel(),
)

fun TrackCacheObject.toModel() = TrackModel(
    filePath = filePath,
    md5 = md5,
    format = TrackFormatModel(
        bitrate = bitrate,
        codec = codec.orEmpty(),
        codecProfile = codecProfile.orEmpty(),
        container = container.orEmpty(),
        duration = duration,
        lossless = lossless,
        numberOfChannels = numberOfChannels,
        sampleRate = sampleRate,
        tagTypes = tagTypes,
        tool = tool.orEmpty(),
    ),
    metadata = TrackMetadataModel(
        album = album.orEmpty(),
        artists = artists.map { ArtistModel(it) },
        genre = genre,
        title = title.orEmpty(),
        number = number ?: 0,
        year = year,
    ),
)

fun TrackModel.toMediaItem() = MediaItem.Builder()
    .setUri(playerUrl)
    .setMediaId(md5)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setArtist(metadata.artist)
            .setTitle(metadata.title)
            .setAlbumTitle(metadata.album)
            .setArtworkUri(
                runCatching { Uri.parse(artworkUrl) }
                    .getOrNull()
            )
            .build()
    )
    .build()
