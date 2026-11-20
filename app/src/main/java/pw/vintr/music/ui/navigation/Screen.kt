package pw.vintr.music.ui.navigation

import pw.vintr.music.core.navigation.screen.Screen as NavigationScreen
import pw.vintr.music.core.navigation.screen.ScreenWithArgs
import pw.vintr.music.core.navigation.screen.buildRouteTemplate
import pw.vintr.music.core.navigation.navArgs.serializable.encodeNavArg
import pw.vintr.music.domain.library.model.album.AlbumModel
import pw.vintr.music.domain.library.model.artist.ArtistModel
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionSheetInfo
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogData

object Screen {

    data object Login : NavigationScreen {
        override val route: String = "login"
    }

    data object Register : NavigationScreen {
        override val route: String = "register"
    }

    data class SelectServer(
        val usePrimaryMountToolbar: Boolean = true,
    ) : ScreenWithArgs(
        destination = ROUTE_DESTINATION,
        args = mapOf(ARG_IS_INITIALIZE_MODE to usePrimaryMountToolbar.toString())
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

    data object ConnectNewServer : NavigationScreen {
        override val route: String = "new-server"
    }

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
        args = mapOf(ARG_KEY_SERVER_INVITE to encodeNavArg(invite))
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

    data object Root : NavigationScreen {
        override val route: String = "root"
    }

    data object Home : NavigationScreen {
        override val route: String = "home"
    }

    data object Search : NavigationScreen {
        override val route: String = "search"
    }

    data object Library : NavigationScreen {
        override val route: String = "library"
    }

    data object Menu : NavigationScreen {
        override val route: String = "menu"
    }

    data object Settings : NavigationScreen {
        override val route: String = "settings"
    }

    data object Equalizer : NavigationScreen {
        override val route: String = "equalizer"
    }

    data object ArtistList : NavigationScreen {
        override val route: String = "all-artists"
    }

    data object PlaylistList : NavigationScreen {
        override val route: String = "all-playlists"
    }

    data object ArtistFavoriteList : NavigationScreen {
        override val route: String = "favorite-artists"
    }

    data object AlbumFavoriteList : NavigationScreen {
        override val route: String = "favorite-albums"
    }

    data object PlaylistCreate : NavigationScreen {
        override val route: String = "playlist-create"
    }

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
        args = mapOf(ARG_KEY_ALBUM to encodeNavArg(album))
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
        args = mapOf(ARG_KEY_ARTIST to encodeNavArg(artist))
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
            ARG_KEY_ARTIST to encodeNavArg(artist),
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
        args = mapOf(
            ARG_KEY_TRACK to encodeNavArg(trackModel),
            ARG_KEY_ACTIONS to encodeNavArg(allowedActions),
        )
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
        args = mapOf(ARG_KEY_SHEET_INFO to encodeNavArg(albumActionSheetInfo))
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
        args = mapOf(ARG_KEY_SHEET_INFO to encodeNavArg(playlistActionSheetInfo))
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
        args = mapOf(ARG_KEY_DATA to encodeNavArg(data))
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

    data object ManageSession : NavigationScreen {
        override val route: String = "manage-session"
    }
}
