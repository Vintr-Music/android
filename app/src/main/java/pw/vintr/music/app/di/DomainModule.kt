package pw.vintr.music.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pw.vintr.music.app.extension.interactor
import pw.vintr.music.domain.equalizer.interactor.EqualizerInteractor
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.domain.library.useCase.GetArtistListUseCase
import pw.vintr.music.domain.library.useCase.SearchLibraryUseCase
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.server.useCase.accessControl.GetServerInviteList
import pw.vintr.music.domain.server.useCase.accessControl.GetServerMemberList
import pw.vintr.music.domain.server.useCase.connectNew.ConnectNewServerUseCase
import pw.vintr.music.domain.server.useCase.selection.GetIsServerSelectedUseCase
import pw.vintr.music.domain.server.useCase.selection.GetSelectedServerUseCase
import pw.vintr.music.domain.server.useCase.list.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.selection.SelectServerUseCase
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase
import pw.vintr.music.domain.user.useCase.GetAuthorizeStateUseCase
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.domain.user.useCase.LogoutUseCase
import pw.vintr.music.domain.user.useCase.RegisterUseCase
import pw.vintr.music.domain.visualizer.interactor.VisualizerInteractor

val domainModule = module {
    single { AuthorizeUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
    single { GetAuthorizeStateUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { GetSelectedServerUseCase(get()) }

    single { GetServerListUseCase(get()) }
    single { SelectServerUseCase(get()) }
    single { GetIsServerSelectedUseCase(get()) }
    single { ConnectNewServerUseCase(get()) }
    single { GetServerInviteList(get()) }
    single { GetServerMemberList(get()) }

    single { GetMainPageContentUseCase(get()) }
    single { GetAlbumTracksUseCase(get()) }
    single { GetArtistListUseCase(get()) }
    single { GetArtistAlbumsUseCase(get()) }
    single { SearchLibraryUseCase(get()) }

    interactor { PlayerInteractor(androidContext(), get(), get()) }
    interactor { EqualizerInteractor(get(), get()) }
    interactor { VisualizerInteractor(get(), get()) }
}
