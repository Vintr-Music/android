package pw.vintr.music.ui.feature.actionSheet.playlist.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.domain.playlist.model.PlaylistModel

@Parcelize
data class PlaylistActionSheetInfo(
    val playlist: PlaylistModel,
    val tracksCount: Int,
    val playDurationMillis: Long,
) : Parcelable
