package pw.vintr.music.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.onClose
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.domain.library.useCase.GetArtistListUseCase
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
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
    single { GetAlbumTracksUseCase(get()) }
    single { GetArtistListUseCase(get()) }
    single { GetArtistAlbumsUseCase(get()) }

    single { PlayerInteractor(androidContext(), get()) } onClose { it?.close() }
}
