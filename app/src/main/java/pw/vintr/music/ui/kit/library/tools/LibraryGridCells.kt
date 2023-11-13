package pw.vintr.music.ui.kit.library.tools

import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun rememberLibraryGridCells(): GridCells.Fixed {
    val count = rememberLibraryGridCellsCount()
    return GridCells.Fixed(count)
}

@Composable
fun rememberLibraryGridCellsCount(): Int {
    val configuration = LocalConfiguration.current

    return remember(key1 = configuration.orientation) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                4
            }
            else -> {
                2
            }
        }
    }
}
