package pw.vintr.music.domain.library.useCase

import pw.vintr.music.data.library.repository.AlbumRepository
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel

class GetArtistAlbumsUseCase(
    private val albumRepository: AlbumRepository,
) {

    suspend operator fun invoke(artist: String): List<AlbumModel> = albumRepository
        .getAlbumsByArtist(artist)
        .map { it.toModel() }
}
