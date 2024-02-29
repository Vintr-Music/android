package pw.vintr.music.data.search.cache

import androidx.annotation.Keep
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

@Keep
class SearchHistoryCacheObject() : RealmObject {

    var queries: RealmList<String> = realmListOf()

    constructor(
        queries: List<String>
    ) : this() {
        this.queries = queries.toRealmList()
    }

    fun toModel() = queries.toList()
}
