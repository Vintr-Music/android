package pw.vintr.music.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pw.vintr.music.ui.feature.login.LoginViewModel

val uiModule = module {
    viewModel { LoginViewModel(get()) }
}
