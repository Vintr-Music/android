package pw.vintr.music.ui.feature.menu.logout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonText
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.Gilroy20
import pw.vintr.music.ui.theme.Gilroy24
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun LogoutConfirmDialog(
    viewModel: LogoutConfirmViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(id = R.string.confirm_logout_title),
            color = VintrMusicExtendedTheme.colors.textRegular,
            textAlign = TextAlign.Center,
            style = Gilroy20
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.confirm_logout_description),
            color = VintrMusicExtendedTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            style = RubikRegular14
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ButtonText(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.common_cancel),
                onClick = { viewModel.navigateBack(type = NavigatorType.Root) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ButtonText(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.logout),
                onClick = { viewModel.logout() }
            )
        }
    }
}
