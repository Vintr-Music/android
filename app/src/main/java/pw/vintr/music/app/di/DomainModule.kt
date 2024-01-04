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
import pw.vintr.music.domain.server.useCase.GetIsServerSelectedUseCase
import pw.vintr.music.domain.server.useCase.GetSelectedServerUseCase
import pw.vintr.music.domain.server.useCase.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.SelectServerUseCase
import pw.vintr.music.domain.user.useCase.AuthorizeUseCase
import pw.vintr.music.domain.user.useCase.GetAuthorizeStateUseCase
import pw.vintr.music.domain.user.useCase.GetProfileUseCase
import pw.vintr.music.domain.user.useCase.LogoutUseCase
import pw.vintr.music.domain.visualizer.VisualizerInteractor

val domainModule = module {
    single { AuthorizeUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
    single { GetAuthorizeStateUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { GetSelectedServerUseCase(get()) }

    single { GetServerListUseCase(get()) }
    single { SelectServerUseCase(get()) }
    single { GetIsServerSelectedUseCase(get()) }

    single { GetMainPageContentUseCase(get()) }
    single { GetAlbumTracksUseCase(get()) }
    single { GetArtistListUseCase(get()) }
    single { GetArtistAlbumsUseCase(get()) }
    single { SearchLibraryUseCase(get()) }

    interactor { PlayerInteractor(androidContext(), get(), get()) }
    interactor { EqualizerInteractor(get(), get()) }
    interactor { VisualizerInteractor(get(), get()) }
}
