package pw.vintr.music.core.navigation.di

import org.koin.dsl.module
import pw.vintr.music.core.navigation.navigator.Navigator

val navigationModule = module {
    single { Navigator() }
}
