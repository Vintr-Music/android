package pw.vintr.music.ui.feature.library

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pw.vintr.music.R
import pw.vintr.music.ui.base.BaseViewModel

class LibraryViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(value = LibraryScreenState())

    val screenState = _screenState.asStateFlow()

    fun selectTab(tab: LibraryTab) {
        _screenState.update { it.copy(selectedTab = tab) }
    }
}

enum class LibraryTab(@StringRes val titleRes: Int) {
    ARTISTS(titleRes = R.string.library_artists),
    ALBUMS(titleRes = R.string.library_albums)
}

data class LibraryScreenState(
    val tabs: List<LibraryTab> = listOf(LibraryTab.ARTISTS, LibraryTab.ALBUMS),
    val selectedTab: LibraryTab = LibraryTab.ARTISTS
) {
    val selectedIndex = tabs.indexOf(selectedTab)
}
