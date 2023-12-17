package pw.vintr.music.ui.feature.equalizer

import android.media.AudioManager
import android.media.audiofx.Equalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.domain.equalizer.model.BandModel
import pw.vintr.music.domain.equalizer.model.EqualizerModel
import pw.vintr.music.domain.equalizer.model.PresetModel
import pw.vintr.music.tools.extension.updateItem
import pw.vintr.music.tools.extension.updateLoaded
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.base.BaseViewModel

class EqualizerViewModel(
    private val audioManager: AudioManager
) : BaseViewModel() {

    private val _screenState = MutableStateFlow<BaseScreenState<EqualizerModel>>(
        value = BaseScreenState.Loading()
    )

    val screenState = _screenState.asStateFlow()

    init {
        initializeEqualizer()
    }

    fun initializeEqualizer() {
        _screenState.loadWithStateHandling {
            val equalizer = Equalizer(0, audioManager.generateAudioSessionId())

            EqualizerModel(
                bands = (0 until equalizer.numberOfBands).map {
                    val bandNumber = it.toShort()
                    val bandRange = equalizer.bandLevelRange

                    BandModel(
                        number = bandNumber,
                        centerFrequency = equalizer.getCenterFreq(bandNumber),
                        lowerLevel = bandRange.first().toInt(),
                        upperLevel = bandRange.last().toInt(),
                        currentLevel = equalizer.getBandLevel(bandNumber).toInt(),
                    )
                },
                presets = (0 until equalizer.numberOfPresets).map {
                    val presetNumber = it.toShort()

                    PresetModel(
                        number = presetNumber,
                        name = equalizer.getPresetName(presetNumber)
                    )
                }
            )
        }
    }

    fun changeBandLevel(band: BandModel, level: Float) {
        _screenState.updateLoaded { equalizer ->
            equalizer.copy(
                bands = equalizer.bands
                    .toMutableList()
                    .updateItem(
                        index = equalizer.bands.indexOf(band)
                    ) { band -> band.copy(currentLevel = level.toInt()) })
        }
    }

    fun applyChanges() {
        // TODO: apply changes & save
    }
}
