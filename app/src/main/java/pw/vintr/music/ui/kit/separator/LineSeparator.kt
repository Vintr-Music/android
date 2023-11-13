package pw.vintr.music.ui.kit.separator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun LineSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = VintrMusicExtendedTheme.colors.lineSeparator),
    )
}
