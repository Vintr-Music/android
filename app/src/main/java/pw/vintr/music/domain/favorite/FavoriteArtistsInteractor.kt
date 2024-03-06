package pw.vintr.music.domain.favorite

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import pw.vintr.music.data.favorite.dto.FavoriteArtistDto
import pw.vintr.music.data.favorite.repository.FavoriteRepository
import pw.vintr.music.domain.base.BaseDomainState
import pw.vintr.music.domain.base.BaseInteractor
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.tools.extension.updateLoaded

class FavoriteArtistsInteractor(
    private val favoriteRepository: FavoriteRepository,
) : BaseInteractor() {

    sealed class Event {

        abstract val artist: ArtistModel

        data class Added(override val artist: ArtistModel) : Event()

        data class Removed(override val artist: ArtistModel) : Event()
    }

    private val _dataFlow: MutableStateFlow<BaseDomainState<List<ArtistModel>>> = MutableStateFlow(
        value = BaseDomainState.Loading()
    )
    val dataFlow = _dataFlow.shareIn(
        scope = this,
        started = SharingStarted.Lazily,
        replay = 1
    )

    private val _events = Channel<Event>()
    val events by lazy { _events.receiveAsFlow() }

    suspend fun loadData() {
        _dataFlow.loadWithStateHandling {
            favoriteRepository
                .getFavoriteArtists()
                .map { ArtistModel(it) }
        }
    }

    suspend fun isInFavorites(artistModel: ArtistModel): Boolean {
        return runCatching {
            favoriteRepository.getArtistIsFavorite(artistModel.name)
        }.getOrElse {
            it.printStackTrace()

            _dataFlow.value.let { freezeData ->
                freezeData is BaseDomainState.Loaded &&
                freezeData.data.contains(artistModel)
            }
        }
    }

    suspend fun addToFavorites(artistModel: ArtistModel) {
        favoriteRepository.addArtistToFavorites(FavoriteArtistDto(artistModel.name))
        _dataFlow.updateLoaded { favorites ->
            listOf(artistModel, *favorites.toTypedArray())
        }
        _events.send(Event.Added(artistModel))
    }

    suspend fun removeFromFavorites(artistModel: ArtistModel) {
        favoriteRepository.removeArtistFromFavorites(FavoriteArtistDto(artistModel.name))
        _dataFlow.updateLoaded { favorites ->
            favorites.filter { it != artistModel }
        }
        _events.send(Event.Removed(artistModel))
    }
}
