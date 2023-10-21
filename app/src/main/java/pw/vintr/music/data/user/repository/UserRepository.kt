package pw.vintr.music.data.user.repository

import pw.vintr.music.data.user.dto.auth.AuthorizeRequestDto
import pw.vintr.music.data.user.source.UserRemoteDataSource

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource
) {

    suspend fun authorize(request: AuthorizeRequestDto) = remoteDataSource.authorize(request)
}
