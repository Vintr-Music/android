package pw.vintr.music.ui.feature.library.artist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.useCase.GetArtistListUseCase
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class ArtistListViewModel(
    private val getArtistListUseCase: GetArtistListUseCase
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<List<ArtistModel>>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling { getArtistListUseCase.invoke() }
    }

    fun refreshData() {
        _screenState.refreshWithStateHandling { getArtistListUseCase.invoke() }
    }

    fun onArtistClick(artist: ArtistModel) {
        navigator.forward(Screen.ArtistDetails(artist))
    }
}
