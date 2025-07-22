package pw.vintr.music.domain.pagination.model

data class PaginationState<T>(
    val currentPage: Int = 1,
    val hasNext: Boolean = false,
    val items: List<T> = listOf(),
    val totalCount: Int = 0,
) {
    val isEmpty get() = items.isEmpty()
}
