package pw.vintr.music.domain.library.useCase

import pw.vintr.music.data.library.repository.SearchRepository
import pw.vintr.music.domain.library.model.search.toModel

class SearchLibraryUseCase(
    private val searchRepository: SearchRepository
) {

    companion object {
        private const val DEFAULT_LIMIT = 50
        private const val DEFAULT_OFFSET = 0
    }

    suspend fun invoke(query: String) = searchRepository
        .searchLibrary(query, DEFAULT_LIMIT, DEFAULT_OFFSET)
        .toModel()
}
