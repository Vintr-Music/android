package pw.vintr.music.ui.feature.actionSheet.album

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
import pw.vintr.music.tools.composable.ColumnItems
import pw.vintr.music.tools.extension.dialogContainer
import pw.vintr.music.tools.format.DurationFormat
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumAction
import pw.vintr.music.ui.feature.actionSheet.album.entity.AlbumActionSheetInfo
import pw.vintr.music.ui.kit.common.ActionSheetInfoParam
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun AlbumActionSheet(
    actionSheetInfo: AlbumActionSheetInfo,
    allowedActions: List<AlbumAction> = listOf(
        AlbumAction.PLAY_NEXT,
        AlbumAction.ADD_TO_QUEUE,
    ),
    viewModel: AlbumActionViewModel = getViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dialogContainer()
    ) {
        // Album main info
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
                        .data(actionSheetInfo.album.artworkUrl)
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
                    text = actionSheetInfo.album.name,
                    style = RubikMedium16,
                    color = VintrMusicExtendedTheme.colors.textRegular
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = actionSheetInfo.album.artistAndYear,
                    style = RubikRegular14,
                    color = VintrMusicExtendedTheme.colors.textSecondary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Album meta info
        ActionSheetInfoParam(
            title = stringResource(id = R.string.playing_time),
            value = DurationFormat.formatSeconds(actionSheetInfo.playDurationMillis),
        )
        Spacer(modifier = Modifier.height(8.dp))
        ActionSheetInfoParam(
            title = stringResource(id = R.string.tracks_count),
            value = actionSheetInfo.tracksCount.toString(),
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
                            AlbumAction.PLAY_NEXT -> {
                                viewModel.playNext()
                            }
                            AlbumAction.ADD_TO_QUEUE -> {
                                viewModel.addToQueue()
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
