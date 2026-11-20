package pw.vintr.music.tools.extension

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <reified R> MutableStateFlow<List<R>>.addItem(item: R) {
    value = value
        .toMutableList()
        .apply { add(item) }
}

inline fun <reified R> MutableStateFlow<List<R>>.removeItem(item: R) {
    value = value
        .toMutableList()
        .apply { remove(item) }
}
