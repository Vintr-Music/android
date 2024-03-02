package pw.vintr.music.ui.kit.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.RubikMedium14
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ActionSheetInfoParam(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = RubikRegular14,
            color = VintrMusicExtendedTheme.colors.textSecondary
        )
        Text(
            text = value,
            style = RubikMedium14,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
    }
}
