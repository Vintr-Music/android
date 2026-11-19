package pw.vintr.music.core.ui.kit.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.ic_playlist_no_artwork

@Composable
fun playlistNoArtworkPainter(): Painter =
    painterResource(Res.drawable.ic_playlist_no_artwork)
