package pw.vintr.music.core.ui.kit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.ic_favorite_filled
import pw.vintr.music.core.ui.resources.ic_favorite_outline
import pw.vintr.music.core.ui.theme.Red2
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonFavorite(
    modifier: Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    ButtonBorderedIcon(
        modifier = modifier,
        icon = if (isFavorite) {
            Res.drawable.ic_favorite_filled
        } else {
            Res.drawable.ic_favorite_outline
        },
        tint = if (isFavorite) {
            Red2
        } else {
            VintrMusicExtendedTheme.colors.textRegular
        },
        onClick = onClick,
    )
}
