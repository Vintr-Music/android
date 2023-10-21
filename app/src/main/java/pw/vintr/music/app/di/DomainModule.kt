package pw.vintr.music.app.di

import org.koin.dsl.module
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase

val domainModule = module {
    single { AuthorizeUseCase(get()) }
}
