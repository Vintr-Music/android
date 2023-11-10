package pw.vintr.music.ui.navigation

import android.os.Parcelable
import androidx.core.os.bundleOf
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel

@Parcelize
sealed class Screen(val route: String) : Parcelable {

    object Login : Screen(route = "login")

    object Register : Screen(route = "register")

    object SelectServer : Screen(route = "select-server")

    object Root : Screen(route = "root")

    object Home : Screen(route = "home")

    object Search : Screen(route = "search")

    object Library : Screen(route = "library")

    object Menu : Screen(route = "menu")

    object Settings : Screen(route = "settings")

    object AlbumDetails : Screen(route = "album-details") {

        @IgnoredOnParcel
        const val ARG_KEY_ALBUM = "arg-key-album"

        fun arguments(album: AlbumModel) = bundleOf(ARG_KEY_ALBUM to album)
    }

    object ArtistDetails : Screen(route = "artist-details") {

        @IgnoredOnParcel
        const val ARG_KEY_ARTIST = "arg-key-artist"

        fun arguments(artist: ArtistModel) = bundleOf(ARG_KEY_ARTIST to artist)
    }
}
