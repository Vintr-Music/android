package pw.vintr.music.ui.navigation

import android.os.Parcelable
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.ui.feature.trackDetails.entity.TrackDetailsOption
import pw.vintr.music.ui.navigation.navArgs.parcelable.ParcelableNavTypeSerializer
import pw.vintr.music.ui.navigation.navArgs.parcelableList.ParcelableListWrapper

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

sealed class Screen(val route: String) {

    abstract class ScreenWithArgs(
        destination: String,
        args: Map<String, Any>
    ) : Screen(
        route = buildRoute(
            destination = destination,
            params = args.mapValues {
                when (val value = it.value) {
                    is Parcelable -> {
                        ParcelableNavTypeSerializer(value.javaClass)
                            .toRouteString(value)
                    }
                    is List<*> -> {
                        val parcelableValues = value.filterIsInstance<Parcelable>()
                        ParcelableNavTypeSerializer(ParcelableListWrapper::class.java)
                            .toRouteString(ParcelableListWrapper(parcelableValues))
                    }
                    else -> {
                        it.value.toString()
                    }
                }
            }
        )
    )

    object Login : Screen(route = "login")

    object Register : Screen(route = "register")

    data class SelectServer(
        val usePrimaryMountToolbar: Boolean = true,
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_IS_INITIALIZE_MODE to usePrimaryMountToolbar)
    ) {
        companion object {
            const val ARG_IS_INITIALIZE_MODE = "arg-is-initialize-mode"
            private const val ROUTE_DESTINATION = "select-server"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_IS_INITIALIZE_MODE)
            )
        }
    }

    object ConnectNewServer : Screen(route = "new-server")

    data class ServerAccessControl(val serverId: String) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_SERVER_ID to serverId)
    ) {
        companion object {
            const val ARG_KEY_SERVER_ID = "arg-key-server-id"
            private const val ROUTE_DESTINATION = "server-access-control"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_SERVER_ID)
            )
        }
    }

    data class ServerInviteDetails(val invite: ServerInviteModel) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_SERVER_INVITE to invite)
    ) {
        companion object {
            const val ARG_KEY_SERVER_INVITE = "arg-server-invite"
            private const val ROUTE_DESTINATION = "server-invite-details"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_SERVER_INVITE)
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

    data class AlbumDetails(val album: AlbumModel) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_ALBUM to album)
    ) {
        companion object {
            const val ARG_KEY_ALBUM = "arg-key-album"
            private const val ROUTE_DESTINATION = "album-details"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_ALBUM)
            )
        }
    }

    data class ArtistDetails(val artist: ArtistModel) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_ARTIST to artist)
    ) {
        companion object {
            const val ARG_KEY_ARTIST = "arg-key-artist"
            private const val ROUTE_DESTINATION = "artist-details"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_ARTIST)
            )
        }
    }

    data class TrackDetails(
        val trackModel: TrackModel,
        val allowedOptions: List<TrackDetailsOption> = listOf(
            TrackDetailsOption.GO_TO_ALBUM,
            TrackDetailsOption.GO_TO_ARTIST
        ),
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_TRACK to trackModel, ARG_KEY_OPTIONS to allowedOptions)
    ) {
        companion object {
            const val ARG_KEY_TRACK = "arg-track-model"
            const val ARG_KEY_OPTIONS = "arg-options"
            private const val ROUTE_DESTINATION = "track-details"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_TRACK, ARG_KEY_OPTIONS)
            )
        }
    }

    object LogoutConfirmDialog : Screen("logout-confirm")
}
