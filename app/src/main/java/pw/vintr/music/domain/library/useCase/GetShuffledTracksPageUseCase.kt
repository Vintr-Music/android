package pw.vintr.music.domain.library.useCase

import pw.vintr.music.app.constants.ConfigConstants.TRACKS_PAGE_SIZE
import pw.vintr.music.data.library.repository.TrackRepository
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.library.model.track.toModel
import pw.vintr.music.domain.pagination.model.PageModel
import pw.vintr.music.domain.pagination.model.toModel

class GetShuffledTracksPageUseCase(
    private val trackRepository: TrackRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        offset: Int = 0,
        artist: String? = null,
    ): PageModel<TrackModel> = trackRepository
        .getShuffledTracksPage(
            sessionId = sessionId,
            offset = offset,
            limit = TRACKS_PAGE_SIZE,
            artist = artist,
        )
        .toModel { it.toModel() }
}
