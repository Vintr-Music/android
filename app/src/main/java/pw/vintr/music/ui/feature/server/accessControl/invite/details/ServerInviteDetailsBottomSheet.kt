package pw.vintr.music.ui.feature.server.accessControl.invite.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.domain.server.model.ServerInviteModel
import pw.vintr.music.tools.extension.dialogContainer
import pw.vintr.music.ui.kit.loader.LoaderScreen
import pw.vintr.music.ui.kit.server.invite.ServerInviteData

@Composable
fun ServerInviteDetailsBottomSheet(
    invite: ServerInviteModel,
    viewModel: ServerInviteDetailsViewModel = koinViewModel { parametersOf(invite) }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dialogContainer()
            .verticalScroll(rememberScrollState())
    ) {
        val state = viewModel.screenState.collectAsState()
        var size by remember<MutableState<IntSize?>> { mutableStateOf(value = null) }

        LaunchedEffect(key1 = size) {
            size?.let { lockedSize ->
                viewModel.loadQR(
                    widthPx = lockedSize.width,
                    heightPx = lockedSize.height
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
                .clip(RoundedCornerShape(4.dp))
                .onGloballyPositioned { size = it.size },
            contentAlignment = Alignment.Center
        ) {
            state.value.qrBitmap?.let { bitmap ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = bitmap.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } ?: run {
                LoaderScreen()
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ServerInviteData(
            modifier = Modifier.fillMaxWidth(),
            state.value.invite
        )
    }
}
