package pw.vintr.music.ui.kit.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.RubikMedium18
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun MenuTitle(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = RubikMedium18,
            color = VintrMusicExtendedTheme.colors.textRegular,
        )
        subtitle?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = Gilroy14,
                color = VintrMusicExtendedTheme.colors.textSecondary,
            )
        }
    }
}
