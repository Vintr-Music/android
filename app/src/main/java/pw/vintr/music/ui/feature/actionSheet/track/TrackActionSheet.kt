package pw.vintr.music.ui.feature.actionSheet.track

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.domain.library.model.track.TrackModel
import pw.vintr.music.tools.composable.ColumnItems
import pw.vintr.music.tools.extension.dialogContainer
import pw.vintr.music.tools.extension.toStringRes
import pw.vintr.music.ui.feature.actionSheet.track.entity.TrackAction
import pw.vintr.music.ui.kit.common.ActionSheetInfoParam
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun TrackActionSheet(
    trackModel: TrackModel,
    allowedActions: List<TrackAction> = listOf(
        TrackAction.GO_TO_ALBUM,
        TrackAction.GO_TO_ARTIST,
        TrackAction.PLAY_NEXT,
        TrackAction.ADD_TO_QUEUE,
    ),
    viewModel: TrackActionViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dialogContainer()
    ) {
        // Track main info
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .size(48.dp)
                    .artworkContainer(RoundedCornerShape(4.dp)),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trackModel.artworkUrl)
                        .size(
                            width = constraints.maxWidth,
                            height = constraints.maxHeight
                        )
                        .crossfade(enable = true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trackModel.metadata.title,
                    style = RubikMedium16,
                    color = VintrMusicExtendedTheme.colors.textRegular
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trackModel.metadata.artist,
                    style = RubikRegular14,
                    color = VintrMusicExtendedTheme.colors.textSecondary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Track meta info
        ActionSheetInfoParam(
            title = stringResource(id = R.string.codec),
            value = trackModel.format.codec,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ActionSheetInfoParam(
            title = stringResource(id = R.string.bitrate),
            value = stringResource(
                id = R.string.bitrate_value,
                trackModel.format.bitrateFormat
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        ActionSheetInfoParam(
            title = stringResource(id = R.string.lossless),
            value = stringResource(id = trackModel.format.lossless.toStringRes()),
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        ColumnItems(
            items = allowedActions,
            spacing = 8.dp
        ) {
            MenuItemIconified(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        when (it) {
                            TrackAction.GO_TO_ALBUM -> {
                                viewModel.openAlbum(trackModel)
                            }
                            TrackAction.GO_TO_ARTIST -> {
                                viewModel.openArtist(trackModel)
                            }
                            TrackAction.PLAY_NEXT -> {
                                viewModel.playNext(trackModel)
                            }
                            TrackAction.ADD_TO_QUEUE -> {
                                viewModel.addToQueue(trackModel)
                            }
                        }
                    }
                    .padding(vertical = 8.dp),
                title = stringResource(id = it.titleRes),
                titleStyle = RubikMedium16,
                iconRes = it.iconRes,
                iconSize = 20.dp,
                iconTint = VintrMusicExtendedTheme.colors.textRegular
            )
        }
    }
}
