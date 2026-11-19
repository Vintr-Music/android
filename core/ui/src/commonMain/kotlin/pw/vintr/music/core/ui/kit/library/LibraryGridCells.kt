package pw.vintr.music.core.ui.kit.library

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun rememberLibraryGridCells(): GridCells.Fixed {
    val count = rememberLibraryGridCellsCount()
    return GridCells.Fixed(count)
}

@Composable
fun rememberLibraryGridCellsCount(): Int {
    val containerSize = LocalWindowInfo.current.containerSize

    return remember(containerSize.width, containerSize.height) {
        if (containerSize.width > containerSize.height) 4 else 2
    }
}
