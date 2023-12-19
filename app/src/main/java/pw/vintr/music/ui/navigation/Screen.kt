package pw.vintr.music.ui.navigation

import android.os.Parcelable
import androidx.core.os.bundleOf
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel

@Suppress("SameParameterValue")
private fun buildRoute(
    destination: String,
    params: Map<String, String> = mapOf()
) = buildString {
    append(destination)
    params.forEach { append("?${it.key}=${it.value}") }
}

@Suppress("SameParameterValue")
private fun buildRouteTemplate(
    destination: String,
    paramKeys: List<String>
) = buildString {
    append(destination)
    paramKeys.forEach { append("?$it={$it}") }
}

@Parcelize
sealed class Screen(val route: String) : Parcelable {

    object Login : Screen(route = "login")

    object Register : Screen(route = "register")

    data class SelectServer(
        val usePrimaryMountToolbar: Boolean = true,
    ) : Screen(
        route = buildRoute(
            destination = ROUTE_DESTINATION,
            params = mapOf(ARG_USE_PRIMARY_TOOLBAR to usePrimaryMountToolbar.toString())
        )
    ) {
        companion object {
            const val ARG_USE_PRIMARY_TOOLBAR = "arg-use-primary-mount-toolbar"
            private const val ROUTE_DESTINATION = "select-server"

            val route = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_USE_PRIMARY_TOOLBAR)
            )
        }
    }

    object Root : Screen(route = "root")

    object Home : Screen(route = "home")

    object Search : Screen(route = "search")

    object Library : Screen(route = "library")

    object Menu : Screen(route = "menu")

    object Settings : Screen(route = "settings")

    object Equalizer : Screen(route = "equalizer")

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

    object TrackDetails : Screen(route = "track-details") {

        @IgnoredOnParcel
        const val ARG_TRACK_MODEL = "arg-track-model"

        fun arguments(trackModel: TrackModel) = bundleOf(ARG_TRACK_MODEL to trackModel)
    }
}
