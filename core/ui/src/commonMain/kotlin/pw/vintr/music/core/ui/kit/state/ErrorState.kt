package pw.vintr.music.core.ui.kit.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pw.vintr.music.core.ui.kit.button.ButtonRegular
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.common_data_error
import pw.vintr.music.core.ui.resources.common_retry
import pw.vintr.music.core.ui.resources.ic_error
import pw.vintr.music.core.ui.theme.Gilroy18
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

@Composable
@Preview
fun ErrorState(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit = { }
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_error),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(Res.string.common_data_error),
            style = Gilroy18,
            color = VintrMusicExtendedTheme.colors.textRegular,
        )
        Spacer(modifier = Modifier.height(32.dp))
        ButtonRegular(
            text = stringResource(Res.string.common_retry),
            onClick = retryAction,
            wrapContentWidth = true,
        )
    }
}
