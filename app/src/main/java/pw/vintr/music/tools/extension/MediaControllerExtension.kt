package pw.vintr.music.tools.extension

import androidx.media3.session.MediaController

fun MediaController?.hasNoItems() = this?.let { it.mediaItemCount == 0 } ?: true
