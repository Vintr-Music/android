package pw.vintr.music.tools.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import pw.vintr.music.ui.navigation.navArgs.parcelableList.ParcelableListWrapper

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle?.getRequiredArg(key: String, clazz: Class<T>): T {
    return requireNotNull(this) { "arguments bundle is null" }.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireNotNull(getParcelable(key, clazz)) { "argument for $key is null" }
        } else {
            requireNotNull(getParcelable(key)) { "argument for $key is null" }
        }
    }
}

@Suppress("DEPRECATION")
@JvmName("getRequiredArgReified")
inline fun <reified T : Parcelable> Bundle?.getRequiredArg(
    key: String,
    clazz: Class<T>? = null,
): T {
    return requireNotNull(this) { "arguments bundle is null" }.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireNotNull(
                getParcelable(key, clazz ?: T::class.java)
            ) { "argument for $key is null" }
        } else {
            requireNotNull(getParcelable(key)) { "argument for $key is null" }
        }
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.getOptionalArg(
    key: String,
    clazz: Class<T>,
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this?.getParcelable(key, clazz)
    } else {
        this?.getParcelable(key)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.getRequiredArgList(key: String): List<T> {
    return requireNotNull(this) { "arguments bundle is null" }.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireNotNull(
                getParcelable(key, ParcelableListWrapper::class.java)
            ) { "argument for $key is null" }.content.filterIsInstance<T>()
        } else {
            requireNotNull(getParcelable(key)) { "argument for $key is null" }
        }
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.getOptionalArgList(
    key: String,
    clazz: Class<T>,
): List<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this?.getParcelableArrayList(key, clazz)
    } else {
        this?.getParcelableArrayList(key)
    }
}
