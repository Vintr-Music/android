package pw.vintr.music.data.favorite.repository

import pw.vintr.music.data.favorite.dto.FavoriteAlbumDto
import pw.vintr.music.data.favorite.dto.FavoriteArtistDto
import pw.vintr.music.data.favorite.source.FavoriteRemoteDataSource
import pw.vintr.music.data.library.dto.album.AlbumDto

class FavoriteRepository(
    private val favoriteRemoteDataSource: FavoriteRemoteDataSource
) {

    suspend fun getAlbumIsFavorite(album: AlbumDto) = favoriteRemoteDataSource
        .getAlbumIsFavorite(album)
        .status

    suspend fun getFavoriteAlbums() = favoriteRemoteDataSource
        .getFavoriteAlbums()

    suspend fun addAlbumToFavorites(request: FavoriteAlbumDto) {
        favoriteRemoteDataSource.addAlbumToFavorites(request)
    }

    suspend fun removeAlbumFromFavorites(request: FavoriteAlbumDto) {
        favoriteRemoteDataSource.removeAlbumFromFavorites(request)
    }

    suspend fun getArtistIsFavorite(artist: String) = favoriteRemoteDataSource
        .getArtistIsFavorite(artist)
        .status

    suspend fun getFavoriteArtists(): List<String> = favoriteRemoteDataSource
        .getFavoriteArtists()

    suspend fun addArtistToFavorites(request: FavoriteArtistDto) {
        favoriteRemoteDataSource.addArtistToFavorites(request)
    }

    suspend fun removeArtistFromFavorites(request: FavoriteArtistDto) {
        favoriteRemoteDataSource.removeArtistFromFavorites(request)
    }
}
