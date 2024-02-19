package pw.vintr.music.ui.kit.server.invite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.tools.format.DateFormat
import pw.vintr.music.ui.theme.Gilroy12
import pw.vintr.music.ui.theme.Gilroy14
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ServerInviteData(
    modifier: Modifier = Modifier,
    invite: ServerInviteModel,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                id = if (invite.isUnlimited) {
                    R.string.invite_unlimited
                } else {
                    R.string.invite_one_time
                }
            ),
            style = Gilroy16,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(
                id = R.string.invite_code,
                invite.code
            ),
            style = Gilroy14,
            color = VintrMusicExtendedTheme.colors.textRegular
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = DateFormat.formatDate(invite.expiry)?.let { formattedDate ->
                stringResource(
                    id = R.string.invite_until,
                    formattedDate
                )
            } ?: stringResource(id = R.string.invite_no_expiration),
            style = Gilroy12,
            color = VintrMusicExtendedTheme.colors.textSecondary
        )
    }
}
