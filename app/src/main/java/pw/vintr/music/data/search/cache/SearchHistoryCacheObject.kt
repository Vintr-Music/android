package pw.vintr.music.data.search.cache

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Singleton search-history document.
 * Previously a RealmObject; now a Room entity with the same CacheObject API surface.
 */
@Keep
@Entity(tableName = "search_history")
data class SearchHistoryCacheObject(
    @PrimaryKey
    val id: Int = SINGLETON_ID,
    val queries: List<String> = emptyList(),
) {

    fun toModel() = queries

    companion object {
        const val SINGLETON_ID = 0
    }
}
