package pw.vintr.music.ui.kit.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.vintr.music.tools.extension.dialogContainer

@Composable
fun DialogContainer(
    onDimClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDimClick?.invoke() }
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
            .copy(alpha = 0.9f)
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .dialogContainer(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
