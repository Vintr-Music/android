package pw.vintr.music.ui.kit.state

import androidx.compose.runtime.Composable
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.kit.loader.LoaderScreen

@Composable
fun <T> ScreenStateHolder(
    state: BaseScreenState<T>,
    loading: @Composable () -> Unit = { LoaderScreen() },
    error: @Composable () -> Unit = { },
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
