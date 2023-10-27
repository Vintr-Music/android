package pw.vintr.music.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext
import pw.vintr.music.app.di.dataModule
import pw.vintr.music.app.di.domainModule
import pw.vintr.music.app.di.uiModule

class App : Application(), ImageLoaderFactory, KoinComponent {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, uiModule))
        }
    }

    override fun newImageLoader(): ImageLoader {
        val okHttpClient: OkHttpClient = get()

        return ImageLoader.Builder(applicationContext)
            .okHttpClient(okHttpClient)
            .crossfade(enable = true)
            .networkObserverEnabled(enable = true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}
