package pw.vintr.music.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pw.vintr.music.app.main.MainViewModel
import pw.vintr.music.ui.feature.albumDetails.AlbumDetailsViewModel
import pw.vintr.music.ui.feature.artistDetails.ArtistDetailsViewModel
import pw.vintr.music.ui.feature.home.HomeViewModel
import pw.vintr.music.ui.feature.library.LibraryViewModel
import pw.vintr.music.ui.feature.library.artistList.ArtistListViewModel
import pw.vintr.music.ui.feature.login.LoginViewModel
import pw.vintr.music.ui.feature.menu.MenuViewModel
import pw.vintr.music.ui.feature.nowPlaying.NowPlayingViewModel
import pw.vintr.music.ui.feature.register.RegisterViewModel
import pw.vintr.music.ui.feature.root.RootViewModel
import pw.vintr.music.ui.feature.search.SearchViewModel
import pw.vintr.music.ui.feature.serverSelection.ServerSelectionViewModel
import pw.vintr.music.ui.feature.settings.SettingsViewModel
import pw.vintr.music.ui.navigation.Navigator

val uiModule = module {
    single { Navigator() }

    viewModel { MainViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel() }
    viewModel { ServerSelectionViewModel(get(), get()) }

    viewModel { RootViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { LibraryViewModel() }
    viewModel { ArtistListViewModel(get()) }
    viewModel { MenuViewModel(get()) }
    viewModel { SettingsViewModel() }
    viewModel { params -> AlbumDetailsViewModel(params.get(), get(), get()) }
    viewModel { params -> ArtistDetailsViewModel(params.get(), get()) }
    viewModel { NowPlayingViewModel(get()) }
}
