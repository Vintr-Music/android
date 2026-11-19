package pw.vintr.music.core.ui.kit.pagination

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pw.vintr.music.core.ui.kit.button.ButtonRegular
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.common_data_error
import pw.vintr.music.core.ui.resources.common_retry
import pw.vintr.music.core.ui.resources.ic_error
import pw.vintr.music.core.ui.theme.Bee1
import pw.vintr.music.core.ui.theme.Gilroy18
import pw.vintr.music.core.ui.theme.VintrMusicExtendedTheme

private const val LOADER_KEY = "PAGE_LOADER"
private const val ERROR_KEY = "PAGE_LOADER"

fun LazyListScope.paginationControls(
    hasNextPage: Boolean,
    hasFailedPage: Boolean,
    retryAction: () -> Unit
) {
    if (hasNextPage && !hasFailedPage) {
        item(key = LOADER_KEY) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = Bee1,
                )
            }
        }
    }

    if (hasFailedPage) {
        item(key = ERROR_KEY) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_error),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(Res.string.common_data_error),
                        style = Gilroy18,
                        color = VintrMusicExtendedTheme.colors.textRegular,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    ButtonRegular(
                        text = stringResource(Res.string.common_retry),
                        onClick = retryAction,
                        wrapContentWidth = true,
                    )
                }
            }
        }
    }
}
