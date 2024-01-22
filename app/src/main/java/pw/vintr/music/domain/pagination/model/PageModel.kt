package pw.vintr.music.domain.pagination.model

import pw.vintr.music.data.pagination.PageDto

data class PageModel<T>(
    val data: List<T>,
    val count: Int,
)

fun <T, U> PageDto<T>.tModel(mapper: (T) -> U) = PageModel(
    data = data.map(mapper),
    count = count,
)
