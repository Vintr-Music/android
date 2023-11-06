package pw.vintr.music.ui.kit.state

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.kit.loader.LoaderScreen

@Composable
fun <T> ScreenStateHolder(
    state: BaseScreenState<T>,
    toolbar: @Composable () -> Unit = { },
    loading: @Composable () -> Unit = {
        Column {
            toolbar()
            LoaderScreen(modifier = Modifier.weight(1f))
        }
    },
    error: @Composable () -> Unit = {
        Column {
            toolbar()
            // TODO: error widget
        }
    },
    other: @Composable () -> Unit = { },
    loaded: @Composable (T) -> Unit,
) {
    when (state) {
        is BaseScreenState.Loading -> {
            loading()
        }
        is BaseScreenState.Error -> {
            error()
        }
        is BaseScreenState.Loaded -> {
            loaded(state.data)
        }
        else -> {
            other()
        }
    }
}
