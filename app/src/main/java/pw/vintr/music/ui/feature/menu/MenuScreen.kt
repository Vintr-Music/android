package pw.vintr.music.ui.feature.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.ui.kit.button.ButtonRegular

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        ButtonRegular(
            text = "Тест",
            onClick = { viewModel.openSettings() }
        )
    }
}
