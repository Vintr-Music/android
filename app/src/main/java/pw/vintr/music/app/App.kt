package pw.vintr.music.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import pw.vintr.music.app.di.dataModule
import pw.vintr.music.app.di.domainModule
import pw.vintr.music.app.di.uiModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, uiModule))
        }
    }
}
