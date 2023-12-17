package pw.vintr.music.data.audioSession.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.vintr.music.data.audioSession.source.AudioSessionPreferencesDataSource

class AudioSessionRepository(
    private val preferencesDataSource: AudioSessionPreferencesDataSource
) {

    private val sessionIdFlow = MutableStateFlow(preferencesDataSource.getSessionId())

    fun setSessionId(sessionId: Int) {
        preferencesDataSource.setSessionId(sessionId)
        sessionIdFlow.value = sessionId
    }

    fun getSessionId() = sessionIdFlow.value

    fun getSessionIdFlow() = sessionIdFlow.asStateFlow()
}
