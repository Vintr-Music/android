package pw.vintr.music.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pw.vintr.music.app.extension.interactor
import pw.vintr.music.domain.alert.interactor.AlertInteractor
import pw.vintr.music.domain.equalizer.interactor.EqualizerInteractor
import pw.vintr.music.domain.favorite.FavoriteAlbumsInteractor
import pw.vintr.music.domain.favorite.FavoriteArtistsInteractor
import pw.vintr.music.domain.library.useCase.GetAlbumTracksUseCase
import pw.vintr.music.domain.library.useCase.GetArtistAlbumsUseCase
import pw.vintr.music.domain.library.useCase.GetArtistListUseCase
import pw.vintr.music.domain.library.useCase.GetShuffledTracksPageUseCase
import pw.vintr.music.domain.library.useCase.SearchLibraryUseCase
import pw.vintr.music.domain.loader.PrimaryLoaderInteractor
import pw.vintr.music.domain.mainPage.useCase.GetMainPageContentUseCase
import pw.vintr.music.domain.player.interactor.PlayerInteractor
import pw.vintr.music.domain.playlist.interactor.PlaylistInteractor
import pw.vintr.music.domain.search.SearchHistoryInteractor
import pw.vintr.music.domain.server.useCase.accessControl.GetServerInviteListUseCase
import pw.vintr.music.domain.server.useCase.accessControl.GetServerInviteQRUseCase
import pw.vintr.music.domain.server.useCase.accessControl.GetServerMemberListUseCase
import pw.vintr.music.domain.server.useCase.connectNew.ConnectNewServerUseCase
import pw.vintr.music.domain.server.useCase.selection.GetIsServerSelectedUseCase
import pw.vintr.music.domain.server.useCase.selection.GetSelectedServerUseCase
import pw.vintr.music.domain.server.useCase.list.GetServerListUseCase
import pw.vintr.music.domain.server.useCase.selection.SelectServerUseCase
import pw.vintr.music.domain.settings.PlaybackSettingsInteractor
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
    single { GetSelectedServerUseCase(get(), get()) }

    single { GetServerListUseCase(get(), get()) }
    single { SelectServerUseCase(get()) }
    single { GetIsServerSelectedUseCase(get()) }
    single { ConnectNewServerUseCase(get()) }
    single { GetServerInviteListUseCase(get()) }
    single { GetServerMemberListUseCase(get()) }
    single { GetServerInviteQRUseCase() }

    single { GetMainPageContentUseCase(get()) }
    single { GetAlbumTracksUseCase(get()) }
    single { GetArtistListUseCase(get()) }
    single { GetArtistAlbumsUseCase(get()) }
    single { SearchLibraryUseCase(get()) }
    single { GetShuffledTracksPageUseCase(get()) }

    interactor { PrimaryLoaderInteractor() }
    interactor { AlertInteractor() }
    interactor { PlayerInteractor(androidContext(), get(), get(), get(), get()) }
    interactor { PlaybackSettingsInteractor(get()) }
    interactor { EqualizerInteractor(get(), get()) }
    interactor { VisualizerInteractor(get(), get()) }
    interactor { SearchHistoryInteractor(get()) }
    interactor { FavoriteAlbumsInteractor(get()) }
    interactor { FavoriteArtistsInteractor(get()) }
    interactor { PlaylistInteractor(get()) }
}
