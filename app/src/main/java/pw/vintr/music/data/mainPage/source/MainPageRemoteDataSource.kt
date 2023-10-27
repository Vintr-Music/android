package pw.vintr.music.data.mainPage.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import pw.vintr.music.data.mainPage.dto.MainPageItemDto

class MainPageRemoteDataSource(private val client: HttpClient) {

    suspend fun getMainPageContent(): List<MainPageItemDto> = client.get {
        url("api/main-page/content")
    }.body()
}
