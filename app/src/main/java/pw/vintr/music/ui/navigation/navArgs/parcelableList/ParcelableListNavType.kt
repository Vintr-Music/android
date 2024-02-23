package pw.vintr.music.ui.navigation.navArgs.parcelableList

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import pw.vintr.music.tools.extension.getRequiredArg
import pw.vintr.music.ui.navigation.navArgs.parcelable.ParcelableNavTypeSerializer

open class ParcelableListNavType<T: Parcelable>: NavType<List<T>>(isNullableAllowed = false) {

    private val serializer: ParcelableNavTypeSerializer = ParcelableNavTypeSerializer(
        ParcelableListWrapper::class.java
    )

    @Suppress("UNCHECKED_CAST")
    override fun get(bundle: Bundle, key: String): List<T> {
        return bundle.getRequiredArg(key, ParcelableListWrapper::class.java).content as List<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): List<T> {
        return (serializer.fromRouteString(value) as ParcelableListWrapper<T>).content
    }

    override fun put(bundle: Bundle, key: String, value: List<T>) {
        bundle.putParcelable(key, ParcelableListWrapper(value))
    }
}
