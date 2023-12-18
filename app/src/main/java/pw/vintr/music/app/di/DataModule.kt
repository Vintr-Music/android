package pw.vintr.music.app.di

import android.util.Log
import androidx.preference.PreferenceManager
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
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.koin.dsl.onClose
import pw.vintr.music.data.audioSession.repository.AudioSessionRepository
import pw.vintr.music.data.audioSession.source.AudioSessionPreferencesDataSource
import pw.vintr.music.data.equalizer.cache.BandCacheObject
import pw.vintr.music.data.equalizer.cache.EqualizerCacheObject
import pw.vintr.music.data.equalizer.cache.PresetCacheObject
import pw.vintr.music.data.equalizer.repository.EqualizerRepository
import pw.vintr.music.data.equalizer.source.EqualizerCacheDataStore
import pw.vintr.music.data.library.cache.album.AlbumCacheObject
import pw.vintr.music.data.library.cache.track.TrackCacheObject
import pw.vintr.music.data.library.repository.AlbumRepository
import pw.vintr.music.data.library.repository.ArtistRepository
import pw.vintr.music.data.library.repository.SearchRepository
import pw.vintr.music.data.library.repository.TrackRepository
import pw.vintr.music.data.library.source.AlbumRemoteDataSource
import pw.vintr.music.data.library.source.ArtistRemoteDataSource
import pw.vintr.music.data.library.source.SearchRemoteDataSource
import pw.vintr.music.data.library.source.TrackRemoteDataSource
import pw.vintr.music.data.mainPage.repository.MainPageRepository
import pw.vintr.music.data.mainPage.source.MainPageRemoteDataSource
import pw.vintr.music.data.player.cache.PlayerSessionCacheObject
import pw.vintr.music.data.player.repository.PlayerConfigRepository
import pw.vintr.music.data.player.repository.PlayerSessionRepository
import pw.vintr.music.data.player.source.PlayerPreferencesDataStore
import pw.vintr.music.data.player.source.PlayerSessionCacheDataStore
import pw.vintr.music.data.server.repository.ServerRepository
import pw.vintr.music.data.server.source.ServerPreferencesDataSource
import pw.vintr.music.data.server.source.ServerRemoteDataSource
import pw.vintr.music.data.user.repository.UserRepository
import pw.vintr.music.data.user.source.UserPreferencesDataSource
import pw.vintr.music.data.user.source.UserRemoteDataSource

private const val BASE_URL = "http://188.225.9.157:4001/"

private const val BEARER_PREFIX = "Bearer "

private const val HEADER_MEDIA_SERVER_ID = "x-media-server-id"

private const val REALM_SCHEMA_VERSION = 1L

val dataModule = module {
    // Preferences
    single { PreferenceManager.getDefaultSharedPreferences(get()) }

    single { UserPreferencesDataSource(get()) }
    single { ServerPreferencesDataSource(get()) }

    single {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                AlbumCacheObject::class,
                TrackCacheObject::class,
                PlayerSessionCacheObject::class,
                PresetCacheObject::class,
                BandCacheObject::class,
                EqualizerCacheObject::class
            )
        )
            .schemaVersion(REALM_SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.open(config)
    } onClose { realm ->
        realm?.close()
    }

    // Network
    single {
        val userPreferencesDataSource: UserPreferencesDataSource = get()
        val serverPreferencesDataSource: ServerPreferencesDataSource = get()

        val httpLogger = object : Logger {
            override fun log(message: String) { Log.v("Logger Ktor =>", message) }
        }

        HttpClient(Android) {
            // Serialization
            install(ContentNegotiation) { gson() }

            // Logging
            install(Logging) {
                logger = httpLogger
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
                userPreferencesDataSource.getAccessToken()?.let { accessToken ->
                    header(HttpHeaders.Authorization, BEARER_PREFIX + accessToken)
                }

                // Media configuration
                serverPreferencesDataSource.getSelectedServerId()?.let { serverId ->
                    header(HEADER_MEDIA_SERVER_ID, serverId)
                }
            }
        }
    }

    single {
        val userPreferencesDataSource: UserPreferencesDataSource = get()
        val serverPreferencesDataSource: ServerPreferencesDataSource = get()

        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()

                userPreferencesDataSource.getAccessToken()?.let { accessToken ->
                    builder.addHeader(
                        HttpHeaders.Authorization,
                        BEARER_PREFIX + accessToken
                    )
                }
                serverPreferencesDataSource.getSelectedServerId()?.let { serverId ->
                    builder.addHeader(HEADER_MEDIA_SERVER_ID, serverId)
                }

                chain.proceed(builder.build())
            }
            .build()
    }

    // User
    single { UserRemoteDataSource(get()) }
    single { UserRepository(get(), get()) }

    // Server
    single { ServerRemoteDataSource(get()) }
    single { ServerRepository(get(), get()) }

    // Main Page
    single { MainPageRemoteDataSource(get()) }
    single { MainPageRepository(get()) }

    // Library
    single { TrackRemoteDataSource(get()) }
    single { TrackRepository(get()) }
    single { ArtistRemoteDataSource(get()) }
    single { ArtistRepository(get()) }
    single { AlbumRemoteDataSource(get()) }
    single { AlbumRepository(get()) }
    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get()) }

    // Audio Session
    single { AudioSessionPreferencesDataSource(get()) }
    single { AudioSessionRepository(get()) }

    // Player
    single { PlayerSessionCacheDataStore(get()) }
    single { PlayerSessionRepository(get()) }
    single { PlayerPreferencesDataStore(get()) }
    single { PlayerConfigRepository(get()) }

    // Equalizer
    single { EqualizerCacheDataStore(get()) }
    single { EqualizerRepository(get()) }
}
