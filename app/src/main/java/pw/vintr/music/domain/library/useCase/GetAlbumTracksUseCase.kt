package pw.vintr.music.domain.library.useCase

import pw.vintr.music.data.library.repository.TrackRepository
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel

class GetAlbumTracksUseCase(
    private val trackRepository: TrackRepository
) {

    suspend operator fun invoke(
        artist: String,
        album: String
    ): List<TrackModel> = trackRepository
        .getTracksByAlbum(artist, album)
        .map { it.toModel() }
}
