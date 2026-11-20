package pw.vintr.music.core.ui.tools.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
expect fun StatusBarEffect(useDarkIcons: Boolean = !isSystemInDarkTheme())
