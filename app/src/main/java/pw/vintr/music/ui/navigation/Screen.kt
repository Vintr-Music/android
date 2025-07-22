package pw.vintr.music.ui.navigation

import android.os.Parcelable
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionSheetInfo
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogData
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
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

    data object Login : Screen(route = "login")

    data object Register : Screen(route = "register")

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

    data object ConnectNewServer : Screen(route = "new-server")

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

    data object Root : Screen(route = "root")

    data object Home : Screen(route = "home")

    data object Search : Screen(route = "search")

    data object Library : Screen(route = "library")

    data object Menu : Screen(route = "menu")

    data object Settings : Screen(route = "settings")

    data object Equalizer : Screen(route = "equalizer")

    data object ArtistList : Screen(route = "all-artists")

    data object PlaylistList : Screen(route = "all-playlists")

    data object ArtistFavoriteList : Screen(route = "favorite-artists")

    data object AlbumFavoriteList : Screen(route = "favorite-albums")

    data object PlaylistCreate : Screen(route = "playlist-create")

    data class PlaylistAddTrack(val trackId: String) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_TRACK_ID to trackId)
    ) {
        companion object {
            const val ARG_KEY_TRACK_ID = "arg-key-track-id"
            private const val ROUTE_DESTINATION = "playlist-add-track"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_TRACK_ID)
            )
        }
    }

    data class PlaylistDetails(val playlistId: String) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_PLAYLIST_ID to playlistId)
    ) {
        companion object {
            const val ARG_KEY_PLAYLIST_ID = "arg-key-playlist-id"
            private const val ROUTE_DESTINATION = "playlist-details"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_PLAYLIST_ID)
            )
        }
    }

    data class PlaylistEdit(val playlistId: String) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_PLAYLIST_ID to playlistId)
    ) {
        companion object {
            const val ARG_KEY_PLAYLIST_ID = "arg-key-playlist-id"
            private const val ROUTE_DESTINATION = "playlist-edit"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_PLAYLIST_ID)
            )
        }
    }

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

    data class ArtistTracks(
        val artist: ArtistModel,
        val playingSessionId: String
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(
            ARG_KEY_ARTIST to artist,
            ARG_KEY_PLAYING_SESSION_ID to playingSessionId,
        )
    ) {
        companion object {
            const val ARG_KEY_ARTIST = "arg-key-artist"
            const val ARG_KEY_PLAYING_SESSION_ID = "arg-key-playing-session-id"
            private const val ROUTE_DESTINATION = "artist-tracks"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_ARTIST, ARG_KEY_PLAYING_SESSION_ID)
            )
        }
    }

    data class TrackActionSheet(
        val trackModel: TrackModel,
        val allowedActions: List<TrackAction> = listOf(
            TrackAction.GO_TO_ALBUM,
            TrackAction.GO_TO_ARTIST,
        ),
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_TRACK to trackModel, ARG_KEY_ACTIONS to allowedActions)
    ) {
        companion object {
            const val ARG_KEY_TRACK = "arg-track-model"
            const val ARG_KEY_ACTIONS = "arg-actions"
            private const val ROUTE_DESTINATION = "track-actions"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_TRACK, ARG_KEY_ACTIONS)
            )
        }
    }

    data class AlbumActionSheet(
        val albumActionSheetInfo: AlbumActionSheetInfo,
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_SHEET_INFO to albumActionSheetInfo)
    ) {
        companion object {
            const val ARG_KEY_SHEET_INFO = "arg-sheet-info"
            private const val ROUTE_DESTINATION = "album-actions"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_SHEET_INFO)
            )
        }
    }

    data class PlaylistActionSheet(
        val playlistActionSheetInfo: PlaylistActionSheetInfo,
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_SHEET_INFO to playlistActionSheetInfo)
    ) {
        companion object {
            const val ARG_KEY_SHEET_INFO = "arg-sheet-info"
            private const val ROUTE_DESTINATION = "playlist-actions"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_SHEET_INFO)
            )
        }
    }

    data class ConfirmDialog(val data: ConfirmDialogData) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_KEY_DATA to data)
    ) {
        companion object {
            const val ARG_KEY_DATA = "arg-key-data"
            private const val ROUTE_DESTINATION = "dialog-confirm"

            val routeTemplate = buildRouteTemplate(
                ROUTE_DESTINATION,
                listOf(ARG_KEY_DATA)
            )
        }
    }

    data object ManageSession : Screen(route = "manage-session")
}
