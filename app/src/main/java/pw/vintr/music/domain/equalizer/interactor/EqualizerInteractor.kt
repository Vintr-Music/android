package pw.vintr.music.domain.equalizer.interactor

import android.media.audiofx.Equalizer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.data.equalizer.repository.EqualizerRepository
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.equalizer.model.EqualizerModel
import pw.vintr.music.domain.equalizer.model.toModel
import pw.vintr.music.tools.extension.toModel

class EqualizerInteractor(
    private val audioSessionRepository: AudioSessionRepository,
    private val equalizerRepository: EqualizerRepository,
) : BaseInteractor() {

    private var mEqualizer: Equalizer? = null

    private var mEqualizerModel: EqualizerModel? = null

    private var lastSessionId = audioSessionRepository.getSessionId()

    init {
        subscribeSessionIdChanges()
    }

    private fun subscribeSessionIdChanges() {
        launch {
            audioSessionRepository
                .getSessionIdFlow()
                .stateIn(this)
                .filter { it != lastSessionId }
                .collectLatest { instantiateEqualizer(it) }
        }
    }

    fun initAsync(sessionId: Int) {
        launch { instantiateEqualizer(sessionId) }
    }

    suspend fun getEqualizer(): EqualizerModel? {
        if (mEqualizerModel == null) {
            instantiateEqualizer(audioSessionRepository.getSessionId())
        }

        return mEqualizerModel
    }

    private suspend fun instantiateEqualizer(sessionId: Int) {
        if (sessionId != -1) {
            lastSessionId = sessionId

            val cachedEqualizer = equalizerRepository
                .getEqualizer()
                ?.toModel()

            mEqualizer = Equalizer(1000, sessionId)

            if (cachedEqualizer != null) {
                mEqualizerModel = cachedEqualizer
                applyEqualizer(cachedEqualizer)
            } else {
                mEqualizerModel = mEqualizer?.toModel()
            }
        }
    }

    suspend fun applyEqualizer(equalizerModel: EqualizerModel) {
        mEqualizer?.let { equalizer ->
            // Set enabled
            equalizer.setEnabled(equalizerModel.enabled)

            // Set bands
            equalizerModel.bands.forEach { band ->
                equalizer.setBandLevel(band.number, band.currentLevel.toShort())
            }
        }
        equalizerRepository.saveEqualizer(equalizerModel.toCacheObject())
        mEqualizerModel = equalizerModel
    }
}
