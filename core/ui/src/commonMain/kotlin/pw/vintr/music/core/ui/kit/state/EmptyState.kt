package pw.vintr.music.core.ui.kit.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pw.vintr.music.core.ui.kit.resource.drawablePainter
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.ic_empty_favorite
import pw.vintr.music.core.ui.theme.Gilroy20
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme
import pw.vintr.music.core.ui.tools.extension.Empty

@Composable
@Preview
fun EmptyState(
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    text: String = String.Empty,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = if (iconRes != null) {
                drawablePainter(iconRes)
            } else {
                painterResource(Res.drawable.ic_empty_favorite)
            },
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            modifier = Modifier.widthIn(max = 280.dp),
            text = text,
            style = Gilroy20,
            color = VintrMusicExtendedTheme.colors.textRegular,
            textAlign = TextAlign.Center,
        )
    }
}
