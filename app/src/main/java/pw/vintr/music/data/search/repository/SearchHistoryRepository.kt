package pw.vintr.music.data.search.repository

import pw.vintr.music.data.search.source.SearchHistoryCacheDataSource

class SearchHistoryRepository(
    private val searchHistoryCacheDataSource: SearchHistoryCacheDataSource
) {

    suspend fun saveSearchHistory(
        history: List<String>
    ) = searchHistoryCacheDataSource.saveSearchHistory(history)

    fun getSearchHistoryFlow() = searchHistoryCacheDataSource
        .getSearchHistoryFlow()
}
