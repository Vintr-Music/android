package pw.vintr.music.ui.kit.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.SimpleIconButton
import pw.vintr.music.ui.theme.Bee1
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ToolbarRegular(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = true,
    onBackPressed: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    backButtonColor: Color = Bee1,
    trailing: @Composable BoxScope.() -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackButton) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                SimpleIconButton(
                    iconRes = R.drawable.ic_back,
                    onClick = onBackPressed,
                    tint = backButtonColor,
                )
            }
        } else {
            Box(modifier = Modifier.size(24.dp))
        }
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = Gilroy18,
            color = VintrMusicExtendedTheme.colors.textRegular,
        )
        Box(modifier = Modifier.size(24.dp)) {
            trailing()
        }
    }
}
