package pw.vintr.music.domain.loader

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import pw.vintr.music.domain.base.BaseInteractor

class PrimaryLoaderInteractor : BaseInteractor() {

    private val _primaryLoaderState = MutableStateFlow(value = false)

    val primaryLoaderState = _primaryLoaderState
        .shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    fun setLoaderState(value: Boolean) {
        _primaryLoaderState.value = value
    }
}
