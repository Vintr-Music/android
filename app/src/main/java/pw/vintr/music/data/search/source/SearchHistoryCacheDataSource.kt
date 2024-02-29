package pw.vintr.music.data.search.source

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.map
import pw.vintr.music.data.search.cache.SearchHistoryCacheObject

class SearchHistoryCacheDataSource(private val realm: Realm) {

    companion object {
        private const val MAX_HISTORY_ITEMS = 20
    }

    suspend fun saveSearchHistory(history: List<String>) {
        realm.write {
            // Find possibly existing equalizer
            val currentHistory = query<SearchHistoryCacheObject>()
                .first()
                .find()

            currentHistory?.let {
                // Update existing history
                currentHistory.queries = currentHistory
                    .queries
                    .toMutableList()
                    .apply { addAll(0, history) }
                    .take(MAX_HISTORY_ITEMS)
                    .distinct()
                    .toRealmList()
            } ?: run {
                // Save new history
                copyToRealm(
                    SearchHistoryCacheObject(
                        history
                            .take(MAX_HISTORY_ITEMS)
                            .distinct()
                    )
                )
            }
        }
    }

    fun getSearchHistoryFlow() = realm.query<SearchHistoryCacheObject>()
        .first()
        .asFlow()
        .map { it.obj }
}
