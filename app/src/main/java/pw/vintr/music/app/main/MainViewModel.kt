package pw.vintr.music.app.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pw.vintr.music.domain.alert.interactor.AlertInteractor
import pw.vintr.music.domain.alert.model.AlertState
import pw.vintr.music.domain.loader.PrimaryLoaderInteractor
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.server.useCase.selection.GetIsServerSelectedUseCase
import pw.vintr.music.domain.user.useCase.GetAuthorizeStateUseCase
import pw.vintr.music.domain.visualizer.interactor.VisualizerInteractor
import pw.vintr.music.ui.base.BaseViewModel
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogTemplate.openSpeakerPlayConfirmDialog

class MainViewModel(
    private val getAuthorizeStateUseCase: GetAuthorizeStateUseCase,
    private val getSelectedServerIdUseCase: GetIsServerSelectedUseCase,
    private val visualizerInteractor: VisualizerInteractor,
    private val playerInteractor: PlayerInteractor,
    private val alertInteractor: AlertInteractor,
    primaryLoaderInteractor: PrimaryLoaderInteractor,
) : BaseViewModel() {

    val initialState = MutableStateFlow(value = getInitialState()).asStateFlow()

    val primaryLoaderState = primaryLoaderInteractor.primaryLoaderState
        .stateInThis(initialValue = false)

    val alertState = alertInteractor.alertState
        .stateInThis(initialValue = AlertState())

    init {
        collectAskPlaySpeakersEvent()
    }

    private fun getInitialState(): AppInitialState {
        val isAuthorized = getAuthorizeStateUseCase.invoke()
        val serverSelected = getSelectedServerIdUseCase.invoke()

        return when {
            isAuthorized && serverSelected -> {
                AppInitialState.Authorized
            }
            isAuthorized -> {
                AppInitialState.ServerSelection
            }
            else -> {
                AppInitialState.Login
            }
        }
    }

    fun setAudioPermissionGranted(isGranted: Boolean) {
        visualizerInteractor.setPermissionGranted(isGranted)
    }

    fun hideAlert() {
        alertInteractor.hideAlert()
    }

    private fun collectAskPlaySpeakersEvent() {
        launch(Dispatchers.Main) {
            playerInteractor.askPlaySpeakersEvent.collectLatest {
                navigator.openSpeakerPlayConfirmDialog {
                    launch(Dispatchers.Main) { it.invoke() }
                }
            }
        }
    }
}

sealed interface AppInitialState {

    object Login : AppInitialState

    object ServerSelection : AppInitialState

    object Authorized : AppInitialState
}
