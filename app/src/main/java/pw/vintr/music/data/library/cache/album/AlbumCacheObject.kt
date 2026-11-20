package pw.vintr.music.data.library.cache.album

import androidx.annotation.Keep
import pw.vintr.music.core.ui.tools.extension.Empty

/**
 * Embedded album fields on [pw.vintr.music.data.player.cache.PlayerSessionCacheObject]
 * (Room @Embedded), matching the previous Realm EmbeddedRealmObject shape.
 */
@Keep
data class AlbumCacheObject(
    val name: String? = null,
    val artist: String = String.Empty,
    val year: Int? = null,
)
