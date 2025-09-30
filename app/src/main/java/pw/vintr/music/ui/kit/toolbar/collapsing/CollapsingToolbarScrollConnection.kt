package pw.vintr.music.ui.kit.toolbar.collapsing

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Modifier.collapsingToolbarScrollConnection(
    coroutineScope: CoroutineScope,
    connection: CollapsingNestedScrollConnection
) = pointerInput(Unit) {
    var previousY = 0f
    var lastDelta = 0f

    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Press -> {
                    previousY = event.changes[0].position.y
                    lastDelta = 0f
                }
                PointerEventType.Move -> {
                    val currentY = event.changes[0].position.y
                    val delta = previousY - currentY
                    val targetOffset = Offset(0f, -delta)

                    if (delta > 0) {
                        connection.onPreScroll(
                            targetOffset,
                            NestedScrollSource.UserInput
                        )
                    } else {
                        connection.onPostScroll(
                            targetOffset,
                            targetOffset,
                            NestedScrollSource.UserInput
                        )
                    }

                    previousY = currentY
                    lastDelta = delta
                }
                PointerEventType.Release -> {
                    if (lastDelta != 0f) {
                        coroutineScope.launch {
                            connection.snap()
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
