package pw.vintr.music.ui.kit.state

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.theme.Gilroy20
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
@Preview
fun EmptyState(
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.ic_empty_favorite,
    text: String = String.Empty,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            modifier = Modifier
                .widthIn(max = 280.dp),
            text = text,
            style = Gilroy20,
            color = VintrMusicExtendedTheme.colors.textRegular,
            textAlign = TextAlign.Center,
        )
    }
}
