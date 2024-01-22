package pw.vintr.music.domain.library.useCase

import pw.vintr.music.data.library.dto.track.TrackDto
import pw.vintr.music.data.library.repository.SearchRepository
import pw.vintr.music.domain.library.model.track.toModel
import pw.vintr.music.domain.pagination.model.tModel

class SearchTracksUseCase(
    private val searchRepository: SearchRepository
) {

    suspend fun invoke(query: String, limit: Int, offset: Int) = searchRepository
        .searchTracks(query, limit, offset)
        .tModel(TrackDto::toModel)
}
