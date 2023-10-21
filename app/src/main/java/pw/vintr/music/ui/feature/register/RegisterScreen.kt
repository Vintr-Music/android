package pw.vintr.music.ui.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = getViewModel()
) {
    StatusBarEffect()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.register),
                onBackPressed = { viewModel.navigateBack() }
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {

        }
    }
}
