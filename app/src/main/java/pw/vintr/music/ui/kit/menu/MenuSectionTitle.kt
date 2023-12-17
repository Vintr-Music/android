package pw.vintr.music.ui.kit.menu

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.vintr.music.ui.theme.RubikMedium18
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun MenuSectionTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier,
        text = title,
        style = RubikMedium18,
        color = VintrMusicExtendedTheme.colors.textRegular
    )
}
