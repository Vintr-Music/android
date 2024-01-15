package pw.vintr.music.ui.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.button.ButtonText
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.toolbar.ToolbarPrimaryMount

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = getViewModel()
) {
    StatusBarEffect(useDarkIcons = true)

    val screenState = viewModel.screenState.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        LogoBar(
            modifier = Modifier
                .height(300.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        AppTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            label = stringResource(id = R.string.login),
            hint = stringResource(id = R.string.login),
            value = screenState.value.login,
            onValueChange = { viewModel.changeLogin(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        AppTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            label = stringResource(id = R.string.password),
            hint = stringResource(id = R.string.password),
            value = screenState.value.password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { viewModel.changePassword(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        ButtonRegular(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.login),
            enabled = screenState.value.formIsValid,
            isLoading = screenState.value.isAuthorizing,
            onClick = { viewModel.authorize() },
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonText(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.register),
            onClick = { viewModel.openRegister() },
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun LogoBar(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        ToolbarPrimaryMount()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Box(
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .size(150.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.ic_logo_large),
                    contentDescription = null,
                )
            }
        }
    }
}
