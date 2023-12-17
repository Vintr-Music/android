package pw.vintr.music.domain.equalizer.interactor

import android.media.audiofx.Equalizer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.equalizer.model.EqualizerModel
import pw.vintr.music.tools.extension.toModel

class EqualizerInteractor(
    private val audioSessionRepository: AudioSessionRepository
) : BaseInteractor() {

    private var mEqualizer: Equalizer? = null

    var mEqualizerModel: EqualizerModel? = null
        private set

    init {
        instantiateEqualizer(audioSessionRepository.getSessionId())
        subscribeSessionIdChanges()
    }

    private fun subscribeSessionIdChanges() {
        launch {
            audioSessionRepository
                .getSessionIdFlow()
                .stateIn(this)
                .collectLatest { instantiateEqualizer(it) }
        }
    }

    private fun instantiateEqualizer(sessionId: Int) {
        if (sessionId != -1) {
            mEqualizer = Equalizer(1000, sessionId)
            mEqualizerModel = mEqualizer?.toModel()

            // TODO: get data from cache
        }
    }

    fun applyEqualizer(equalizerModel: EqualizerModel) {
        mEqualizer?.let { equalizer ->
            // Set enabled
            equalizer.setEnabled(equalizerModel.enabled)

            // Set bands
            equalizerModel.bands.forEach { band ->
                equalizer.setBandLevel(band.number, band.currentLevel.toShort())
            }
        }
        mEqualizerModel = equalizerModel
    }
}
