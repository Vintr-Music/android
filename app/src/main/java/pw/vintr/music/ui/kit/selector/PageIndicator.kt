package pw.vintr.music.ui.kit.selector

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    selectedPageIndex: Int = 0
) {
    Row(
        modifier = modifier
            .height(8.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (index in 0 until pageCount) {
            PageDot(
                isSelected = selectedPageIndex == index
            )
        }
    }
}

@Composable
private fun PageDot(
    isSelected: Boolean,
) {
    val color = animateColorAsState(
        if (isSelected) {
            VintrMusicExtendedTheme.colors.navBarSelected
        } else {
            VintrMusicExtendedTheme.colors.navBarUnselected
        }
    )

    val height = animateDpAsState(if (isSelected) 8.dp else 4.dp)

    Box(
        modifier = Modifier
            .width(4.dp)
            .height(height.value)
            .background(color.value)
    )
}
