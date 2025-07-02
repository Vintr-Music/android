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
        flowSessionId: String,
        offset: Int = 0,
    ): PageModel<TrackModel> = trackRepository
        .getShuffledTracksPage(
            flowSessionId = flowSessionId,
            offset = offset,
            limit = TRACKS_PAGE_SIZE
        )
        .toModel { it.toModel() }
}
