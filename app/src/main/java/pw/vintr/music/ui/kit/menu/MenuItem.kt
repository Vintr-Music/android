package pw.vintr.music.ui.kit.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.Bee0

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    leading: @Composable (RowScope.() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        leading?.invoke(this)
        MenuTitle(
            modifier = Modifier.weight(1f),
            title = title,
            subtitle = subtitle
        )
        trailing?.invoke(this)
    }
}

@Composable
fun MenuItemIconified(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    @DrawableRes iconRes: Int,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    MenuItem(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        leading = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = iconRes),
                tint = Bee0,
                contentDescription = null
            )
        },
        trailing = trailing,
    )
}
