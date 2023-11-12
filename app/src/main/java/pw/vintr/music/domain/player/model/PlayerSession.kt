package pw.vintr.music.domain.player.model

import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel

sealed class PlayerSession {

    abstract val tracks: List<TrackModel>

    data class PlayerAlbumSession(
        val album: AlbumModel,
        override val tracks: List<TrackModel>,
    ) : PlayerSession()

    data class PlayerArtistSession(
        val artist: ArtistModel,
        override val tracks: List<TrackModel>,
    ) : PlayerSession()
}
