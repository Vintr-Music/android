package pw.vintr.music.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.DebugLogger
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext
import pw.vintr.music.BuildConfig
import pw.vintr.music.app.di.dataModule
import pw.vintr.music.app.di.domainModule
import pw.vintr.music.app.di.uiModule

class App : Application(), ImageLoaderFactory, KoinComponent {

    override fun onCreate() {
        super.onCreate()

        // DI start
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, uiModule))
        }

        // App Metrica activate
        if (!BuildConfig.DEBUG) {
            AppMetrica.activate(
                this,
                AppMetricaConfig
                    .newConfigBuilder(BuildConfig.METRICA_API_KEY)
                    .build()
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        val okHttpClient: OkHttpClient = get()

        return ImageLoader.Builder(applicationContext)
            .logger(DebugLogger())
            .okHttpClient(okHttpClient)
            .crossfade(enable = true)
            .networkObserverEnabled(enable = true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}
