package pw.vintr.music.ui.kit.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.vintr.music.ui.base.BaseScreenState
import pw.vintr.music.ui.kit.loader.LoaderScreen
import pw.vintr.music.ui.kit.state.ErrorState

@Composable
fun <T> ScreenStateLayout(
    modifier: Modifier = Modifier,
    state: BaseScreenState<T>,
    errorRetryAction: () -> Unit = {},
    toolbar: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {
        Column(
            modifier = modifier
        ) {
            toolbar()
            LoaderScreen(modifier = Modifier.weight(1f))
        }
    },
    error: @Composable () -> Unit = {
        Column(
            modifier = modifier
        ) {
            toolbar()
            ErrorState(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                retryAction = errorRetryAction,
            )
        }
    },
    empty: @Composable () -> Unit = {},
    other: @Composable (BaseScreenState<T>) -> Unit = {},
    loaded: @Composable (BaseScreenState.Loaded<T>) -> Unit,
) {
    when (state) {
        is BaseScreenState.Loading -> {
            loading()
        }
        is BaseScreenState.Error -> {
            error()
        }
        is BaseScreenState.Loaded -> {
            loaded(state)
        }
        is BaseScreenState.Empty -> {
            empty()
        }
        else -> {
            other(state)
        }
    }
}
