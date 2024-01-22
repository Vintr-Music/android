package pw.vintr.music.domain.pagination.controller

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.pagination.model.PageModel
import pw.vintr.music.domain.pagination.model.PaginationState
import pw.vintr.music.tools.extension.cancelIfActive

class PaginationController<T>(
    private val limit: Int = DEFAULT_LIMIT,
    private val interaction: suspend (limit: Int, offset: Int) -> PageModel<T>,
) {

    companion object {
        private const val DEFAULT_LIMIT = 50
        private const val FIRST_PAGE = 0
    }

    private val _pagingState = MutableStateFlow(value = PaginationState<T>())

    val pagingState = _pagingState.asStateFlow()

    private var loadPageJob: Job? = null

    private var lock = true

    private val currentPage get() = _pagingState.value.currentPage

    val hasNext get() = _pagingState.value.hasNext

    suspend fun loadFirstPage() {
        loadPageJob.cancelIfActive()
        coroutineScope {
            loadPageJob = launch { loadData(page = FIRST_PAGE) }
        }
    }

    suspend fun loadNextPage() {
        if (hasNext && !lock) {
            coroutineScope {
                loadPageJob = launch { loadData(page = currentPage + 1) }
            }
        }
    }

    suspend fun loadData(page: Int) {
        runCatching {
            lock = true

            val isFirstPage = page == FIRST_PAGE
            val offset = page * limit

            val data = interaction(limit, offset)

            if (isFirstPage) {
                _pagingState.value = PaginationState(
                    items = data.data,
                    currentPage = page,
                    hasNext = data.data.size < data.count,
                )
            } else {
                val mergedItems = _pagingState.value.items + data.data

                _pagingState.value = _pagingState.value.copy(
                    items = mergedItems,
                    currentPage = page,
                    hasNext = mergedItems.size < data.count,
                )
            }

            lock = false
        }.onFailure {
            lock = false
            throw it
        }
    }
}
