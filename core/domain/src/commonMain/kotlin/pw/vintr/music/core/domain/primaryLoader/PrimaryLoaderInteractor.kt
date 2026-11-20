package pw.vintr.music.core.domain.primaryLoader

import kotlinx.coroutines.flow.SharedFlow

interface PrimaryLoaderInteractor {

    val primaryLoaderState: SharedFlow<Boolean>

    fun setLoaderState(value: Boolean)
}
