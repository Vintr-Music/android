package pw.vintr.music.ui.kit.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.theme.Gilroy18
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

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
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.common_data_error),
            style = Gilroy18,
            color = VintrMusicExtendedTheme.colors.textRegular,
        )
        Spacer(modifier = Modifier.height(32.dp))
        ButtonRegular(
            text = stringResource(id = R.string.common_retry),
            onClick = retryAction,
            wrapContentWidth = true,
        )
    }
}
