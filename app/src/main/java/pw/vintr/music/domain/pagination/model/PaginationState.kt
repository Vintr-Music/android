package pw.vintr.music.domain.pagination.model

data class PaginationState<T>(
    val currentPage: Int = 1,
    val hasNext: Boolean = false,
    val items: List<T> = listOf(),
)
