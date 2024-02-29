package pw.vintr.music.domain.search

import kotlinx.coroutines.flow.map
import pw.vintr.music.data.search.repository.SearchHistoryRepository
import pw.vintr.music.domain.base.BaseInteractor

class SearchHistoryInteractor(
    private val searchHistoryRepository: SearchHistoryRepository
) : BaseInteractor() {

    suspend fun saveQuery(query: String) {
        searchHistoryRepository.saveSearchHistory(listOf(query))
    }

    fun getSearchQueryFlow() = searchHistoryRepository
        .getSearchHistoryFlow()
        .map { it?.toModel() ?: listOf() }
}
