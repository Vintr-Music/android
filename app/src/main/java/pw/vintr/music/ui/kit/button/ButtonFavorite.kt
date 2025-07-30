package pw.vintr.music.ui.kit.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.vintr.music.R
import pw.vintr.music.ui.theme.Red2
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ButtonFavorite(
    modifier: Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    ButtonBorderedIcon(
        modifier = modifier,
        iconRes = if (isFavorite) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.ic_favorite_outline
        },
        tint = if (isFavorite) {
            Red2
        } else {
            VintrMusicExtendedTheme.colors.textRegular
        },
        onClick = onClick,
    )
}
