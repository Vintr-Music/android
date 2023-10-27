package pw.vintr.music.app.di

import org.koin.dsl.module
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.domain.server.useCase.GetSelectedServerIdUseCase
import pw.vintr.music.domain.server.useCase.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.SelectServerUseCase
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase
import pw.vintr.music.domain.user.useCase.GetAuthorizeStateUseCase
import pw.vintr.music.domain.user.useCase.GetProfileUseCase

val domainModule = module {
    single { AuthorizeUseCase(get()) }
    single { GetAuthorizeStateUseCase(get()) }
    single { GetProfileUseCase(get()) }

    single { GetServerListUseCase(get()) }
    single { SelectServerUseCase(get()) }
    single { GetSelectedServerIdUseCase(get()) }

    single { GetMainPageContentUseCase(get()) }
}
