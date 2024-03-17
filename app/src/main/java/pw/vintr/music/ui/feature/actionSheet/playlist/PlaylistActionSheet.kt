package pw.vintr.music.ui.feature.actionSheet.playlist

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
import coil.request.CachePolicy
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel
import pw.vintr.music.R
import pw.vintr.music.tools.composable.ColumnItems
import pw.vintr.music.tools.extension.dialogContainer
import pw.vintr.music.tools.format.DurationFormat
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistAction
import pw.vintr.music.ui.feature.actionSheet.playlist.entity.PlaylistActionSheetInfo
import pw.vintr.music.ui.kit.common.ActionSheetInfoParam
import pw.vintr.music.ui.kit.menu.MenuItemIconified
import pw.vintr.music.ui.kit.modifier.artworkContainer
import pw.vintr.music.ui.theme.RubikMedium16
import pw.vintr.music.ui.theme.RubikRegular14
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme

@Composable
fun PlaylistActionSheet(
    actionSheetInfo: PlaylistActionSheetInfo,
    allowedActions: List<PlaylistAction> = listOf(
        PlaylistAction.PLAY_NEXT,
        PlaylistAction.ADD_TO_QUEUE,
        PlaylistAction.EDIT_PLAYLIST,
        PlaylistAction.DELETE_PLAYLIST
    ),
    viewModel: PlaylistActionViewModel = getViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dialogContainer()
    ) {
        // Playlist main info
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
                        .data(actionSheetInfo.playlist.artworkUrl)
                        .size(
                            width = constraints.maxWidth,
                            height = constraints.maxHeight
                        )
                        .crossfade(enable = true)
                        .error(drawableResId = R.drawable.ic_playlist_no_artwork)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .diskCachePolicy(CachePolicy.DISABLED)
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
                    text = actionSheetInfo.playlist.name,
                    style = RubikMedium16,
                    color = VintrMusicExtendedTheme.colors.textRegular
                )
                if (actionSheetInfo.playlist.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = actionSheetInfo.playlist.description,
                        style = RubikRegular14,
                        color = VintrMusicExtendedTheme.colors.textSecondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Playlist meta info
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
                            PlaylistAction.PLAY_NEXT -> {
                                viewModel.playNext()
                            }
                            PlaylistAction.ADD_TO_QUEUE -> {
                                viewModel.addToQueue()
                            }
                            PlaylistAction.EDIT_PLAYLIST -> {
                                viewModel.editPlaylist()
                            }
                            PlaylistAction.DELETE_PLAYLIST -> {
                                viewModel.deletePlaylist()
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
