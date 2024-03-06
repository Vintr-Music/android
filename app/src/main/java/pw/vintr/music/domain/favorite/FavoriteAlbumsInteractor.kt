package pw.vintr.music.domain.favorite

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import pw.vintr.music.data.favorite.dto.FavoriteAlbumDto
import pw.vintr.music.data.favorite.repository.FavoriteRepository
import pw.vintr.music.domain.base.BaseDomainState
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel
import pw.vintr.music.tools.extension.updateLoaded

class FavoriteAlbumsInteractor(
    private val favoriteRepository: FavoriteRepository,
) : BaseInteractor() {

    sealed class Event {

        abstract val album: AlbumModel

        data class Added(override val album: AlbumModel) : Event()

        data class Removed(override val album: AlbumModel) : Event()
    }

    private val _dataFlow: MutableStateFlow<BaseDomainState<List<AlbumModel>>> = MutableStateFlow(
        value = BaseDomainState.Loading()
    )
    val dataFlow = _dataFlow.shareIn(
        scope = this,
        started = SharingStarted.Lazily,
        replay = 1
    )

    private val _events = Channel<Event>()
    val events by lazy {
        _events
            .receiveAsFlow()
            .shareIn(scope = this, started = SharingStarted.Lazily)
    }

    suspend fun loadData() {
        _dataFlow.loadWithStateHandling {
            favoriteRepository
                .getFavoriteAlbums()
                .map { it.toModel() }
        }
    }

    suspend fun isInFavorites(albumModel: AlbumModel): Boolean {
        return runCatching {
            favoriteRepository.getAlbumIsFavorite(albumModel.toDto())
        }.getOrElse {
            it.printStackTrace()

            _dataFlow.value.let { freezeData ->
                freezeData is BaseDomainState.Loaded &&
                freezeData.data.contains(albumModel)
            }
        }
    }

    suspend fun addToFavorites(albumModel: AlbumModel) {
        favoriteRepository.addAlbumToFavorites(FavoriteAlbumDto(albumModel.toDto()))
        _dataFlow.updateLoaded { favorites ->
            listOf(albumModel, *favorites.toTypedArray())
        }
        _events.send(Event.Added(albumModel))
    }

    suspend fun removeFromFavorites(albumModel: AlbumModel) {
        favoriteRepository.removeAlbumFromFavorites(FavoriteAlbumDto(albumModel.toDto()))
        _dataFlow.updateLoaded { favorites ->
            favorites.filter { it != albumModel }
        }
        _events.send(Event.Removed(albumModel))
    }
}
