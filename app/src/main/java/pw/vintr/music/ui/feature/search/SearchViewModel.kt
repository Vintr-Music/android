package pw.vintr.music.ui.feature.search

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.base.BaseViewModel

class SearchViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenState())

    val screenState = _screenState.asStateFlow()

    fun changeQuery(value: String) {
        _screenState.update { it.copy(query = value) }
    }
}

data class SearchScreenState(
    val query: String = String.Empty
)
