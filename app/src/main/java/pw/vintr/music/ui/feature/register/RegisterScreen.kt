package pw.vintr.music.ui.feature.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = getViewModel()
) {
    StatusBarEffect()

    val screenState = viewModel.screenState.collectAsState()

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
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.login_field),
                    hint = stringResource(id = R.string.login_field),
                    value = screenState.value.login,
                    onValueChange = { viewModel.changeLogin(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.password),
                    hint = stringResource(id = R.string.enter_password),
                    value = screenState.value.password,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { viewModel.changePassword(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    )
                )
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.repeat_password),
                    hint = stringResource(id = R.string.repeat_password),
                    value = screenState.value.repeatPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { viewModel.changeRepeatPassword(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    )
                )
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.first_name),
                    hint = stringResource(id = R.string.first_name),
                    value = screenState.value.firstName,
                    onValueChange = { viewModel.changeFirstName(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    )
                )
                AppTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    label = stringResource(id = R.string.last_name),
                    hint = stringResource(id = R.string.last_name),
                    value = screenState.value.lastName,
                    onValueChange = { viewModel.changeLastName(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonRegular(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.make_register),
                enabled = screenState.value.formIsValid,
                isLoading = screenState.value.isRegistering,
                onClick = { viewModel.register() },
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
