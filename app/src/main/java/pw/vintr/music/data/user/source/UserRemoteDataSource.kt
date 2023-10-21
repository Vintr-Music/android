package pw.vintr.music.data.user.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import pw.vintr.music.data.user.dto.auth.AuthorizeRequestDto
import pw.vintr.music.data.user.dto.auth.AuthorizeResponseDto

class UserRemoteDataSource(private val client: HttpClient) {

    suspend fun authorize(
        request: AuthorizeRequestDto
    ): AuthorizeResponseDto = client.post {
        url("gate/user/auth/login")
        setBody(request)
    }.body()
}
