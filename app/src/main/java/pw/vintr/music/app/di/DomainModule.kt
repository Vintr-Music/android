package pw.vintr.music.app.di

import org.koin.dsl.module
import pw.vintr.music.domain.server.useCase.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.SelectServerUseCase
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase

val domainModule = module {
    single { AuthorizeUseCase(get()) }

    single { GetServerListUseCase(get()) }
    single { SelectServerUseCase(get()) }
}
