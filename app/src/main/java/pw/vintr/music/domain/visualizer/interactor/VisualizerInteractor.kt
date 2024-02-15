package pw.vintr.music.domain.visualizer.interactor

import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.player.model.state.PlayerStatusModel
import pw.vintr.music.domain.visualizer.model.VisualizerState

class VisualizerInteractor(
    private val audioSessionRepository: AudioSessionRepository,
    playerInteractor: PlayerInteractor
) : BaseInteractor() {

    data class VisualizerInitData(
        val sessionId: Int,
        val permissionGranted: Boolean,
    )

    private var mVisualizer: Visualizer? = null

    private var permissionStateFlow = MutableStateFlow(value = false)

    private var visualizerEnabledFlow = MutableStateFlow(value = false)

    private val visualizerDataFlow = MutableStateFlow<List<Byte>>(listOf())

    val visualizerStateFlow = combine(
        visualizerEnabledFlow,
        visualizerDataFlow,
        playerInteractor.playerState
    ) { enabled, data, playerState ->
        val showVisualizer = enabled && playerState.currentTrack != null

        if (playerState.status == PlayerStatusModel.PLAYING && showVisualizer) {
            VisualizerState(
                bytes = data,
                enabled = true
            )
        } else {
            VisualizerState(
                bytes = listOf(),
                enabled = showVisualizer
            )
        }
    }.shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    init {
        subscribeChanges()
    }

    private fun subscribeChanges() {
        launch {
            combine(
                audioSessionRepository.getSessionIdFlow(),
                permissionStateFlow
            ) { sessionId, permissionGranted ->
                VisualizerInitData(sessionId, permissionGranted)
            }.collectLatest {
                instantiateVisualizer(it)
            }
        }
    }

    private fun instantiateVisualizer(data: VisualizerInitData) {
        if (data.sessionId != -1 && data.permissionGranted) {
            mVisualizer?.release()
            mVisualizer = null

            mVisualizer = Visualizer(data.sessionId)
            mVisualizer?.setCaptureSize(Visualizer.getCaptureSizeRange()[1])
            mVisualizer?.setDataCaptureListener(object : OnDataCaptureListener {
                override fun onWaveFormDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
                    visualizerDataFlow.value = (p1 ?: byteArrayOf()).toList()
                }

                override fun onFftDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
                    visualizerDataFlow.value = (p1 ?: byteArrayOf()).toList()
                }
            }, Visualizer.getMaxCaptureRate() / 2, false, true)

            mVisualizer?.setEnabled(true)
            visualizerEnabledFlow.value = true
        } else {
            mVisualizer?.release()
            mVisualizer = null
            visualizerEnabledFlow.value = false
        }
    }

    fun setPermissionGranted(permissionGranted: Boolean) {
        permissionStateFlow.value = permissionGranted
    }

    override fun close() {
        mVisualizer?.release()
        mVisualizer = null
        super.close()
    }
}
