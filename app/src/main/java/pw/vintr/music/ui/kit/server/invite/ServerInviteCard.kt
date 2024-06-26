package pw.vintr.music.ui.kit.server.invite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.ui.kit.modifier.cardContainer
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ServerInviteCard(
    modifier: Modifier = Modifier,
    invite: ServerInviteModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardContainer(
                shape = RoundedCornerShape(10.dp),
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (invite.isUnlimited) {
                    R.drawable.ic_invite_unlimited
                } else {
                    R.drawable.ic_invite_personal
                }
            ),
            tint = VintrMusicExtendedTheme.colors.textRegular,
            contentDescription = null
        )
        ServerInviteData(
            modifier = Modifier
                .weight(1f),
            invite = invite
        )
    }
}
