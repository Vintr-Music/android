package pw.vintr.music.ui.feature.equalizer

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pw.vintr.music.domain.equalizer.interactor.EqualizerInteractor
import pw.vintr.music.domain.equalizer.model.BandModel
import pw.vintr.music.domain.equalizer.model.EqualizerModel
import pw.vintr.music.tools.extension.cancelIfActive
import pw.vintr.music.tools.extension.updateItem
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.tools.extension.withLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

class EqualizerViewModel(
    private val equalizerInteractor: EqualizerInteractor,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<EqualizerModel>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    private var saveEqualizerJob: Job? = null

    init {
        initializeEqualizer()
    }

    fun initializeEqualizer() {
        _screenState.loadWithStateHandling {
            requireNotNull(equalizerInteractor.getEqualizer())
        }
    }

    fun changeUseEqualizer(useEqualizer: Boolean) {
        _screenState.updateLoaded { equalizer ->
            equalizer.copy(enabled = useEqualizer)
        }
        applyChanges()
    }

    fun changeBandLevel(band: BandModel, level: Float) {
        _screenState.updateLoaded { equalizer ->
            equalizer.copy(
                bands = equalizer.bands
                    .toMutableList()
                    .updateItem(
                        index = equalizer.bands.indexOf(band)
                    ) { band -> band.copy(currentLevel = level.toInt()) },
                enabled = true
            )
        }
    }

    fun applyChanges() {
        saveEqualizerJob.cancelIfActive()
        saveEqualizerJob = launch {
            _screenState.withLoaded { equalizer ->
                equalizerInteractor.applyEqualizer(equalizer)
            }
        }
    }
}
