package pw.vintr.music.ui.feature.dialog

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
import pw.vintr.music.ui.feature.dialog.entity.ConfirmDialogData
import pw.vintr.music.ui.kit.button.ButtonText
import pw.vintr.music.ui.theme.Gilroy20
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ConfirmDialog(
    data: ConfirmDialogData,
    viewModel: ConfirmViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = when (data) {
                is ConfirmDialogData.Resource -> {
                    stringResource(id = data.titleRes)
                }
                is ConfirmDialogData.Text -> {
                    data.title
                }
            },
            color = VintrMusicExtendedTheme.colors.textRegular,
            textAlign = TextAlign.Center,
            style = Gilroy20
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = when (data) {
                is ConfirmDialogData.Resource -> {
                    stringResource(id = data.messageRes)
                }
                is ConfirmDialogData.Text -> {
                    data.message
                }
            },
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
                text = when (data) {
                    is ConfirmDialogData.Resource -> {
                        data.declineTextRes?.let { stringResource(id = it) }

                    }
                    is ConfirmDialogData.Text -> {
                        data.declineText
                    }
                } ?: stringResource(id = R.string.common_cancel),
                onClick = { viewModel.decline() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ButtonText(
                modifier = Modifier.weight(1f),
                text = when (data) {
                    is ConfirmDialogData.Resource -> {
                        data.acceptTextRes?.let { stringResource(id = it) }

                    }
                    is ConfirmDialogData.Text -> {
                        data.acceptText
                    }
                } ?: stringResource(id = R.string.common_ok),
                onClick = { viewModel.accept() }
            )
        }
    }
}
