package pw.vintr.music.ui.kit.server

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.ui.kit.button.ButtonSecondary
import pw.vintr.music.ui.kit.button.ButtonSecondarySize
import pw.vintr.music.ui.kit.radio.AppRadioButton
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ServerSelectableItem(
    server: ServerModel,
    selected: Boolean,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp),
    canShowAccessControlButton: Boolean = true,
    onAccessControlClick: () -> Unit = {},
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                ),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_server_cloud),
                contentDescription = null
            )
            Text(
                modifier = Modifier.weight(1f),
                text = server.name,
                style = Gilroy14,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            AppRadioButton(selected = selected, onClick = onClick)
        }
        if (server.haveAccessControl && canShowAccessControlButton) {
            ButtonSecondary(
                modifier = Modifier
                    .padding(
                        start = padding.calculateStartPadding(LocalLayoutDirection.current),
                        end = padding.calculateEndPadding(LocalLayoutDirection.current),
                    ),
                text = stringResource(id = R.string.access_control),
                size = ButtonSecondarySize.MEDIUM,
                wrapContentWidth = true,
                onClick = onAccessControlClick,
            )
        }
    }
}
