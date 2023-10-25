package pw.vintr.music.app.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson
import org.koin.dsl.module
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.data.user.source.UserRemoteDataSource

private const val BASE_URL = "http://188.225.9.157:4001/"

val dataModule = module {
    // Network
    val httpClient = HttpClient(Android) {
        // Serialization
        install(ContentNegotiation) { gson() }

        // Logging
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) { Log.v("Logger Ktor =>", message) }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            // Base configuration
            url(BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)

            // Auth configuration

            // Media configuration
        }
    }

    single { httpClient }

    // User
    single { UserRemoteDataSource(get()) }
    single { UserRepository(get()) }
}
