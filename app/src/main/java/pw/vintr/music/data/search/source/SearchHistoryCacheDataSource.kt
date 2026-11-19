package pw.vintr.music.data.search.source

import kotlinx.coroutines.flow.Flow
import pw.vintr.music.data.search.cache.SearchHistoryCacheObject
import pw.vintr.music.data.search.cache.SearchHistoryDao

/**
 * Public API intentionally unchanged vs the Realm implementation:
 * - suspend saveSearchHistory(List&lt;String&gt;)
 * - Flow&lt;SearchHistoryCacheObject?&gt; getSearchHistoryFlow()
 */
class SearchHistoryCacheDataSource(
    private val searchHistoryDao: SearchHistoryDao,
) {

    companion object {
        private const val MAX_HISTORY_ITEMS = 20
    }

    suspend fun saveSearchHistory(history: List<String>) {
        val currentQueries = searchHistoryDao.get()?.queries.orEmpty()
        val merged = (history + currentQueries)
            .distinct()
            .take(MAX_HISTORY_ITEMS)

        searchHistoryDao.upsert(
            SearchHistoryCacheObject(queries = merged)
        )
    }

    fun getSearchHistoryFlow(): Flow<SearchHistoryCacheObject?> =
        searchHistoryDao.observe()
}
