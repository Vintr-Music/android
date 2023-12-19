package pw.vintr.music.ui.navigation.navargs.parcelable

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import pw.vintr.music.tools.extension.getRequiredArg

open class ParcelableNavType<T: Parcelable>(
    private val clazz: Class<T>
): NavType<T>(isNullableAllowed = false) {

    private val serializer: ParcelableNavTypeSerializer = ParcelableNavTypeSerializer(clazz)

    override fun get(bundle: Bundle, key: String): T {
        return bundle.getRequiredArg(key, clazz)
    }

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): T {
        return serializer.fromRouteString(value) as T
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}
