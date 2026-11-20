package pw.vintr.music.core.domain.primaryLoader

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import pw.vintr.music.core.domain.base.BaseInteractor

class PrimaryLoaderInteractorImpl : BaseInteractor(), PrimaryLoaderInteractor {

    private val _primaryLoaderState = MutableStateFlow(value = false)

    override val primaryLoaderState = _primaryLoaderState
        .shareIn(scope = this, started = SharingStarted.Lazily, replay = 1)

    override fun setLoaderState(value: Boolean) {
        _primaryLoaderState.value = value
    }
}
