package pw.vintr.music.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pw.vintr.music.app.main.MainViewModel
import pw.vintr.music.ui.feature.actionSheet.album.AlbumActionViewModel
import pw.vintr.music.ui.feature.actionSheet.playlist.PlaylistActionViewModel
import pw.vintr.music.ui.feature.albumDetails.AlbumDetailsViewModel
import pw.vintr.music.ui.feature.artistDetails.ArtistDetailsViewModel
import pw.vintr.music.ui.feature.dialog.ConfirmViewModel
import pw.vintr.music.ui.feature.equalizer.EqualizerViewModel
import pw.vintr.music.ui.feature.home.HomeViewModel
import pw.vintr.music.ui.feature.library.LibraryViewModel
import pw.vintr.music.ui.feature.library.artist.ArtistListViewModel
import pw.vintr.music.ui.feature.login.LoginViewModel
import pw.vintr.music.ui.feature.menu.MenuViewModel
import pw.vintr.music.ui.feature.nowPlaying.NowPlayingViewModel
import pw.vintr.music.ui.feature.nowPlaying.manageSession.ManageSessionViewModel
import pw.vintr.music.ui.feature.register.RegisterViewModel
import pw.vintr.music.ui.feature.root.RootViewModel
import pw.vintr.music.ui.feature.search.SearchViewModel
import pw.vintr.music.ui.feature.server.accessControl.ServerAccessControlViewModel
import pw.vintr.music.ui.feature.server.accessControl.invite.ServerInviteListViewModel
import pw.vintr.music.ui.feature.server.accessControl.invite.details.ServerInviteDetailsViewModel
import pw.vintr.music.ui.feature.server.accessControl.members.ServerMemberListViewModel
import pw.vintr.music.ui.feature.server.selection.ServerSelectionViewModel
import pw.vintr.music.ui.feature.server.selection.connectNew.ConnectNewServerViewModel
import pw.vintr.music.ui.feature.settings.SettingsViewModel
import pw.vintr.music.ui.feature.actionSheet.track.TrackActionViewModel
import pw.vintr.music.ui.feature.library.favorite.albumFavoriteList.AlbumFavoriteListViewModel
import pw.vintr.music.ui.feature.library.favorite.artistFavoriteList.ArtistFavoriteListViewModel
import pw.vintr.music.ui.feature.library.playlist.PlaylistListViewModel
import pw.vintr.music.ui.feature.library.playlist.addTrack.PlaylistAddTrackViewModel
import pw.vintr.music.ui.feature.library.playlist.create.PlaylistCreateViewModel
import pw.vintr.music.ui.feature.library.playlist.details.PlaylistDetailsViewModel
import pw.vintr.music.ui.feature.library.playlist.edit.PlaylistEditViewModel
import pw.vintr.music.ui.navigation.Navigator

val uiModule = module {
    single { Navigator() }

    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }

    viewModel { ServerSelectionViewModel(get(), get(), get(), get()) }
    viewModel { ConnectNewServerViewModel(get(), get()) }
    viewModel { ServerAccessControlViewModel() }
    viewModel { params ->
        ServerInviteListViewModel(
            serverId = params.get(),
            getServerInviteListUseCase = get()
        )
    }
    viewModel { params ->
        ServerInviteDetailsViewModel(
            invite = params.get(),
            getServerInviteQRUseCase = get()
        )
    }
    viewModel { params ->
        ServerMemberListViewModel(
            serverId = params.get(),
            getServerMemberListUseCase = get()
        )
    }

    viewModel { RootViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { ArtistListViewModel(get()) }
    viewModel { PlaylistListViewModel(get()) }
    viewModel { PlaylistCreateViewModel(get()) }
    viewModel { params ->
        PlaylistAddTrackViewModel(
            trackId = params.get(),
            playlistInteractor = get(),
            alertInteractor = get(),
        )
    }
    viewModel { ArtistFavoriteListViewModel(get()) }
    viewModel { AlbumFavoriteListViewModel(get()) }
    viewModel { MenuViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { params ->
        PlaylistDetailsViewModel(
            playlistId = params.get(),
            playerInteractor = get(),
            playlistInteractor = get(),
        )
    }
    viewModel {params ->
        PlaylistEditViewModel(
            playlistId = params.get(),
            playlistInteractor = get(),
        )
    }
    viewModel { params ->
        AlbumDetailsViewModel(
            album = params.get(),
            playerInteractor = get(),
            getAlbumTracksUseCase = get(),
            favoriteAlbumsInteractor = get()
        )
    }
    viewModel { params ->
        ArtistDetailsViewModel(
            artist = params.get(),
            getArtistAlbumsUseCase = get(),
            favoriteArtistsInteractor = get()
        )
    }
    viewModel { NowPlayingViewModel(get()) }
    viewModel { EqualizerViewModel(get()) }
    viewModel { ConfirmViewModel() }
    viewModel { ManageSessionViewModel(get()) }

    viewModel { TrackActionViewModel(get()) }
    viewModel { AlbumActionViewModel() }
    viewModel { PlaylistActionViewModel() }
}
