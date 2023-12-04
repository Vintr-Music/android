package pw.vintr.music.domain.library.model.search

import pw.vintr.music.data.library.dto.search.SearchContentDto
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel

data class SearchContent(
    val artists: List<ArtistModel>,
    val albums: List<AlbumModel>,
    val tracks: List<TrackModel>,
)

fun SearchContentDto.toModel() = SearchContent(
    artists = artists.map { ArtistModel(it) },
    albums = albums.map { it.toModel() },
    tracks = tracks.map { it.toModel() }
)
