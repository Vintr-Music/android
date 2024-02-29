package pw.vintr.music.tools.extension

fun <T: Any> List<T>.updateItem(
    index: Int,
    mutation: (T) -> T,
) = toMutableList().also { list ->
    if (index in 0 until list.size) {
        list[index] = mutation(list[index])
    }
}

fun <T: Any> List<T>.reorder(from: Int, to: Int) = toMutableList()
    .apply { add(to, removeAt(from)) }
