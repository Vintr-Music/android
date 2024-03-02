package pw.vintr.music.ui.feature.actionSheet.album.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pw.vintr.music.domain.library.model.album.AlbumModel

@Parcelize
data class AlbumActionSheetInfo(
    val album: AlbumModel,
    val tracksCount: Int,
    val playDurationMillis: Long,
) : Parcelable
