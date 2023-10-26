package pw.vintr.music.ui.kit.server

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.server.model.ServerModel
import pw.vintr.music.ui.kit.radio.AppRadioButton
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ServerSelectableItem(
    server: ServerModel,
    selected: Boolean,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp),
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(padding),
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
}
