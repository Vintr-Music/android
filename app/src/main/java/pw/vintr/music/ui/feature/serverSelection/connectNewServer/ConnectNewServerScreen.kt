package pw.vintr.music.ui.feature.serverSelection.connectNewServer

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.scanner.QRScanner
import pw.vintr.music.ui.kit.selector.SegmentControl
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.theme.Gilroy16
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConnectNewServerScreen(
    viewModel: ConnectNewServerViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.add_new_server),
                onBackPressed = { viewModel.navigateBack() }
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

            SegmentControl(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(36.dp),
                items = ConnectNewServerTabType.records
                    .map { stringResource(id = it.tabTitleRes) },
                selectedItemIndex = screenState.value.tabData.tabType.ordinal,
                onSelectedTab = { viewModel.selectTab(ConnectNewServerTabType.records[it]) }
            )

            when (val data = screenState.value.tabData) {
                is ConnectNewServerTabData.QR -> {
                    val cameraPermissionState = rememberPermissionState(
                        permission = Manifest.permission.CAMERA
                    )
                    val localContext = LocalContext.current
                    val cameraProviderFeature = remember {
                        ProcessCameraProvider.getInstance(localContext)
                    }

                    // Permission effect
                    LaunchedEffect(Unit) { cameraPermissionState.launchPermissionRequest() }

                    // Handler to hide qr scanner before back navigation
                    BackHandler { viewModel.navigateBack() }

                    if (
                        cameraPermissionState.status.isGranted &&
                        !screenState.value.hideQRCamera
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 36.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(ratio = 1f)
                            ) {
                                QRScanner(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .clipToBounds(),
                                    externalCameraProviderFeature = cameraProviderFeature,
                                    onQRCodeScanned = { viewModel.onQRCodeScanned(it) }
                                )
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.scanner_frame),
                                    contentDescription = null,
                                )
                            }
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(
                                text = stringResource(id = R.string.point_camera_at_qr),
                                color = VintrMusicExtendedTheme.colors.textRegular,
                                style = Gilroy16
                            )
                        }
                    }
                }
                is ConnectNewServerTabData.Manual -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            AppTextField(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                label = stringResource(id = R.string.server_name_title),
                                hint = stringResource(id = R.string.server_name_hint),
                                value = data.serverName,
                                onValueChange = { viewModel.changeServerName(it) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next,
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            AppTextField(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                label = stringResource(id = R.string.invite_code_title),
                                hint = stringResource(id = R.string.invite_code_hint),
                                value = data.inviteCode,
                                onValueChange = { viewModel.changeInviteCode(it) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        ButtonRegular(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = stringResource(id = R.string.server_make_connection),
                            enabled = data.formIsValid,
                            onClick = { viewModel.connectManual() },
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
