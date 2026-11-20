package pw.vintr.music.core.navigation.navArgs.serializable

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import pw.vintr.music.core.navigation.navArgs.NavTypeSerializer

class SerializableNavTypeSerializer<T : Any>(
    private val serializer: KSerializer<T>
) : NavTypeSerializer<T> {

    override fun toRouteString(value: T): String = encodeNavArg(value, serializer)

    override fun fromRouteString(routeStr: String): T = decodeNavArg(routeStr, serializer)
}

class SerializableNavType<T : Any>(
    private val serializer: KSerializer<T>
) : NavType<T>(isNullableAllowed = false) {

    private val navSerializer = SerializableNavTypeSerializer(serializer)

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write {
            putString(key, serializeAsValue(value))
        }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read {
            getStringOrNull(key)
        }?.let(navSerializer::fromRouteString)
    }

    override fun parseValue(value: String): T = navSerializer.fromRouteString(value)

    override fun serializeAsValue(value: T): String = navSerializer.toRouteString(value)
}

class SerializableListNavType<T : Any>(
    private val serializer: KSerializer<List<T>>
) : NavType<List<T>>(isNullableAllowed = false) {

    private val navSerializer = SerializableNavTypeSerializer(serializer)

    override fun put(bundle: SavedState, key: String, value: List<T>) {
        bundle.write {
            putString(key, serializeAsValue(value))
        }
    }

    override fun get(bundle: SavedState, key: String): List<T>? {
        return bundle.read {
            getStringOrNull(key)
        }?.let(navSerializer::fromRouteString)
    }

    override fun parseValue(value: String): List<T> = navSerializer.fromRouteString(value)

    override fun serializeAsValue(value: List<T>): String = navSerializer.toRouteString(value)
}

inline fun <reified T : Any> serializableNavType(): SerializableNavType<T> =
    SerializableNavType(serializer())

inline fun <reified T : Any> serializableListNavType(): SerializableListNavType<T> =
    SerializableListNavType(serializer())
