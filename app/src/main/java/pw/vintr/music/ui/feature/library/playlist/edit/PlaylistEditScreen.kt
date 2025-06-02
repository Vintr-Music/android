package pw.vintr.music.ui.feature.library.playlist.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pw.vintr.music.R
import pw.vintr.music.tools.composable.rememberBottomSheetNestedScrollInterceptor
import pw.vintr.music.ui.kit.button.ButtonSecondary
import pw.vintr.music.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.kit.layout.ScreenStateLayout
import pw.vintr.music.ui.kit.library.TrackView
import pw.vintr.music.ui.kit.toolbar.ToolbarRegular
import pw.vintr.music.ui.navigation.NavigatorType
import pw.vintr.music.ui.theme.VintrMusicExtendedTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private const val KEY_NAME = "name"
private const val KEY_DESCRIPTION = "description"

@Composable
fun PlaylistEditScreen(
    playlistId: String,
    viewModel: PlaylistEditViewModel = koinViewModel { parametersOf(playlistId) }
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            ToolbarRegular(
                title = stringResource(id = R.string.playlist_edit),
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
        val screenState = viewModel.screenState.collectAsState()

        ScreenStateLayout(
            modifier = Modifier.padding(scaffoldPadding),
            state = screenState.value,
            errorRetryAction = { viewModel.loadData() },
            loaded = { state ->
                val screenData = state.data

                fun Int.toPosition() = (this - 2).coerceAtLeast(minimumValue = 0)

                val lazyListState = rememberLazyListState()
                val reorderableLazyColumnState = rememberReorderableLazyListState(
                    lazyListState
                ) { from, to ->
                    viewModel.reorder(from.index.toPosition(), to.index.toPosition())
                }

                Column(
                    modifier = Modifier
                        .nestedScroll(rememberBottomSheetNestedScrollInterceptor(lazyListState))
                        .fillMaxSize()
                        .imePadding()
                        .padding(scaffoldPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 20.dp),
                        state = lazyListState,
                    ) {
                        // Name field
                        item(key = KEY_NAME) {
                            AppTextField(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                label = stringResource(id = R.string.playlist_name),
                                hint = stringResource(id = R.string.playlist_name_hint),
                                value = screenData.name,
                                onValueChange = { viewModel.changeName(it) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next,
                                )
                            )
                        }

                        // Description field
                        item(key = KEY_DESCRIPTION) {
                            AppTextField(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(bottom = 12.dp),
                                label = stringResource(id = R.string.playlist_description),
                                hint = stringResource(id = R.string.playlist_description_hint),
                                value = screenData.description,
                                onValueChange = { viewModel.changeDescription(it) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Done,
                                )
                            )
                        }

                        // Tracks
                        items(
                            items = screenData.records,
                            key = { item -> item.id }
                        ) { item ->
                            ReorderableItem(
                                reorderableLazyColumnState,
                                key = item.id,
                            ) {
                                TrackView(
                                    trackModel = item.track,
                                    trailingAction = {
                                        Icon(
                                            modifier = Modifier.draggableHandle(),
                                            painter = painterResource(id = R.drawable.ic_drag),
                                            tint = VintrMusicExtendedTheme.colors.textRegular,
                                            contentDescription = null
                                        )
                                    },
                                )
                            }
                        }
                    }
                    ButtonSecondary(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = stringResource(id = R.string.common_save),
                        enabled = screenData.canBeSaved,
                        onClick = { viewModel.savePlaylist() },
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        )
    }
}
