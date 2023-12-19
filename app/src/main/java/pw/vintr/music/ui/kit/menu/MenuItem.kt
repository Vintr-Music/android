package pw.vintr.music.ui.kit.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.Bee0
import pw.vintr.music.ui.theme.RubikMedium18
import pw.vintr.music.ui.theme.switchColors

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    titleStyle: TextStyle = RubikMedium18,
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
            subtitle = subtitle,
            titleStyle = titleStyle,
        )
        trailing?.invoke(this)
    }
}

@Composable
fun MenuItemIconified(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    titleStyle: TextStyle = RubikMedium18,
    iconTint: Color = Bee0,
    iconSize: Dp = 24.dp,
    @DrawableRes iconRes: Int,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    MenuItem(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        titleStyle = titleStyle,
        leading = {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = iconRes),
                tint = iconTint,
                contentDescription = null
            )
        },
        trailing = trailing,
    )
}

@Composable
fun MenuItemSwitchable(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    MenuItem(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        trailing = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = switchColors())
        },
    )
}
