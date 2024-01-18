package pw.vintr.music.data.user.repository

import pw.vintr.music.data.user.dto.auth.AuthorizeRequestDto
import pw.vintr.music.data.user.dto.register.RegisterRequestDto
import pw.vintr.music.data.user.source.UserPreferencesDataSource
import pw.vintr.music.data.user.source.UserRemoteDataSource

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource,
    private val preferencesDataSource: UserPreferencesDataSource
) {

    suspend fun authorize(request: AuthorizeRequestDto) = remoteDataSource.authorize(request)

    suspend fun register(request: RegisterRequestDto) = remoteDataSource.register(request)

    fun setAccessToken(token: String) = preferencesDataSource.setAccessToken(token)

    fun getAccessToken() = preferencesDataSource.getAccessToken()

    fun removeAccessToken() = preferencesDataSource.removeAccessToken()

    suspend fun getProfile() = remoteDataSource.getProfile()
}
