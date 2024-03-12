package pw.vintr.music.ui.feature.library.playlist.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType

@Composable
fun PlaylistCreateScreen(
    viewModel: PlaylistCreateViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.playlist_new),
                showBackButton = false,
                trailing = {
                    ButtonSimpleIcon(
                        iconRes = R.drawable.ic_close,
                        onClick = { viewModel.navigateBack(NavigatorType.Root) },
                    )
                }
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(scaffoldPadding)
        ) {
            val screenState = viewModel.screenState.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.playlist_name),
                    hint = stringResource(id = R.string.playlist_name_hint),
                    value = screenState.value.name,
                    onValueChange = { viewModel.changeName(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next,
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.playlist_description),
                    hint = stringResource(id = R.string.playlist_description_hint),
                    value = screenState.value.description,
                    onValueChange = { viewModel.changeDescription(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonRegular(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.playlist_create),
                enabled = screenState.value.formIsValid,
                isLoading = screenState.value.isCreating,
                onClick = { viewModel.createPlaylist() },
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
