package pw.vintr.music.data.cache.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Stores nested [List]<[String]> as a JSON TEXT column.
 * Mirrors the previous RealmList&lt;String&gt; document shape without a join table.
 */
class StringListConverter {

    private val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return gson.fromJson(value, type) ?: emptyList()
    }
}
