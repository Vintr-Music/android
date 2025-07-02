package pw.vintr.music.ui.feature.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.mainPage.model.MainPageItemModel
import pw.vintr.music.domain.mainPage.model.MainPageWelcomeModel
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.session.PlayerSessionModel
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.visualizer.interactor.VisualizerInteractor
import pw.vintr.music.domain.visualizer.model.VisualizerState
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.navigation.Screen

class HomeViewModel(
    private val getMainPageContentUseCase: GetMainPageContentUseCase,
    private val playerInteractor: PlayerInteractor,
    visualizerInteractor: VisualizerInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<HomeScreenData>>(
        value = BaseScreenState.Loading()
    )
    val screenState = _screenState.asStateFlow()

    private val _isLoadingFlow = MutableStateFlow(false)
    val flowPlayingState = combine(
        _isLoadingFlow,
        playerInteractor.playerState,
    ) { isLoading, playerState ->
        if (playerState.session is PlayerSessionModel.Flow) {
            FlowPlayingState(
                playingTrack = playerState.currentTrack,
                playerStatus = if (isLoading) {
                    PlayerStatusModel.LOADING
                } else {
                    playerState.status
                },
                currentIsFlow = playerState.currentTrack != null,
            )
        } else {
            FlowPlayingState(
                playingTrack = playerState.currentTrack,
                playerStatus = if (isLoading) {
                    PlayerStatusModel.LOADING
                } else {
                    PlayerStatusModel.IDLE
                }
            )
        }
    }.stateInThis(FlowPlayingState())

    val visualizerState = visualizerInteractor.visualizerStateFlow
        .stateInThis(initialValue = VisualizerState())

    init {
        loadData()
    }

    fun loadData() {
        _screenState.loadWithStateHandling { loadScreenData() }
    }

    fun refreshData() {
        _screenState.refreshWithStateHandling { loadScreenData() }
    }

    fun playFlow() {
        if (flowPlayingState.value.currentIsFlow) {
            playerInteractor.resume()
        } else {
            playNewFlow()
        }
    }

    fun pauseFlow() {
        playerInteractor.pause()
    }

    fun playNewFlow() {
        launch(createExceptionHandler()) {
            withLoading(
                setLoadingCallback = {
                    _isLoadingFlow.value = it
                },
                action = {
                    playerInteractor.startNewFlowSession()
                }
            )
        }
    }

    private suspend fun loadScreenData() = HomeScreenData(
        welcome = MainPageWelcomeModel.getByNowTime(),
        items = getMainPageContentUseCase.invoke(),
    )

    fun onAlbumClick(album: AlbumModel) {
        navigator.forward(Screen.AlbumDetails(album))
    }
}

data class HomeScreenData(
    val welcome: MainPageWelcomeModel,
    val items: List<MainPageItemModel>,
)

data class FlowPlayingState(
    val playingTrack: TrackModel? = null,
    val playerStatus: PlayerStatusModel = PlayerStatusModel.IDLE,
    val currentIsFlow: Boolean = false,
)
