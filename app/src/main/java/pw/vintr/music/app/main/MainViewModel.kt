package pw.vintr.music.app.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.server.useCase.GetIsServerSelectedUseCase
import pw.vintr.music.domain.user.useCase.GetAuthorizeStateUseCase
import pw.vintr.music.domain.visualizer.VisualizerInteractor
import pw.vintr.music.ui.base.BaseViewModel

class MainViewModel(
    private val getAuthorizeStateUseCase: GetAuthorizeStateUseCase,
    private val getSelectedServerIdUseCase: GetIsServerSelectedUseCase,
    private val visualizerInteractor: VisualizerInteractor,
) : BaseViewModel() {

    val initialState = MutableStateFlow(value = getInitialState()).asStateFlow()

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
}

sealed interface AppInitialState {

    object Login : AppInitialState

    object ServerSelection : AppInitialState

    object Authorized : AppInitialState
}
