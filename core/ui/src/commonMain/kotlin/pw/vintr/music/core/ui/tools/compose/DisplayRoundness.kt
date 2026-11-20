package pw.vintr.music.core.ui.tools.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
expect fun rememberDisplayRoundness(defaultValue: Dp = 0.dp): Dp
