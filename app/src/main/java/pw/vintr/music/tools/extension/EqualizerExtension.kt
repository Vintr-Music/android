package pw.vintr.music.tools.extension

import android.media.audiofx.Equalizer
import pw.vintr.music.domain.equalizer.model.BandModel
import pw.vintr.music.domain.equalizer.model.EqualizerModel
import pw.vintr.music.domain.equalizer.model.PresetModel

fun Equalizer.toModel() = EqualizerModel(
    bands = (0 until numberOfBands).map {
        val bandNumber = it.toShort()
        val bandRange = bandLevelRange

        BandModel(
            number = bandNumber,
            centerFrequency = getCenterFreq(bandNumber),
            lowerLevel = bandRange.first().toInt(),
            upperLevel = bandRange.last().toInt(),
            currentLevel = getBandLevel(bandNumber).toInt(),
        )
    },
    presets = (0 until numberOfPresets).map {
        val presetNumber = it.toShort()

        PresetModel(
            number = presetNumber,
            name = getPresetName(presetNumber)
        )
    }
)
