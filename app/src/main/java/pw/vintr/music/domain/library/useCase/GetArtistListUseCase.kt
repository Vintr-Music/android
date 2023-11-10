package pw.vintr.music.domain.library.useCase

import pw.vintr.music.data.library.repository.ArtistRepository
import pw.vintr.music.domain.library.model.artist.ArtistModel

class GetArtistListUseCase(
    private val artistRepository: ArtistRepository
) {
    suspend operator fun invoke(): List<ArtistModel> {
        return artistRepository
            .getArtistList()
            .map { ArtistModel(it) }
    }
}
