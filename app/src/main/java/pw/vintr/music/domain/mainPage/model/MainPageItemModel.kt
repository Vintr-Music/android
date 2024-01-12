package pw.vintr.music.domain.mainPage.model

import androidx.compose.runtime.Stable
import pw.vintr.music.data.mainPage.dto.MainPageItemDto
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.album.toModel

@Stable
data class MainPageItemModel(
    val artist: String,
    val albums: List<AlbumModel>
)

fun MainPageItemDto.toModel() = MainPageItemModel(
    artist = artist,
    albums = albums.map { it.toModel() }
)
