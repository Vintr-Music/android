package pw.vintr.music.ui.kit.server.member

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.domain.user.model.UserModel
import pw.vintr.music.ui.kit.modifier.cardContainer
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.RubikBold12
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun ServerMemberCard(
    modifier: Modifier = Modifier,
    member: UserModel,
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
        Image(
            painter = painterResource(id = R.drawable.ic_server_member),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = member.fullName,
                style = Gilroy16,
                color = VintrMusicExtendedTheme.colors.textRegular
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.member_login_template, member.login),
                style = RubikBold12,
                color = VintrMusicExtendedTheme.colors.textSecondary
            )
        }
    }
}
