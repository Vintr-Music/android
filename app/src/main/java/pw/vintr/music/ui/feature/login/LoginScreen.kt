package pw.vintr.music.ui.feature.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pw.vintr.music.R
import pw.vintr.music.tools.composable.StatusBarEffect
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.theme.Gray5

@Composable
fun LoginScreen() {
    StatusBarEffect(useDarkIcons = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        LogoBar(
            modifier = Modifier
                .height(300.dp)
        )
        AppTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            label = "Email",
            hint = "Email"
        )
        Spacer(modifier = Modifier.height(20.dp))
        AppTextField(
            modifier = Modifier.padding(horizontal = 20.dp),
            label = "Пароль",
            hint = "Пароль"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        ButtonRegular(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Войти",
            onClick = { },
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LogoBar(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = 200.dp),
            onDraw = {
                val rect = Rect(Offset.Zero, size)
                val fillRect = Rect(
                    left = Offset.Zero.x,
                    top = Offset.Zero.y,
                    right = size.width,
                    bottom = size.height - 120.dp.toPx()
                )
                val path = Path().apply {
                    val xDisplacedCenter = Offset(
                        x = rect.bottomCenter.x + (rect.bottomCenter.x / 2),
                        y = rect.bottomCenter.y
                    )
                    moveTo(rect.topLeft.x, rect.topLeft.y)
                    lineTo(rect.bottomLeft.x, rect.bottomLeft.y - 80.dp.toPx())
                    lineTo(xDisplacedCenter.x - 20, xDisplacedCenter.y - 20.dp.toPx())
                    lineTo(rect.bottomRight.x, rect.bottomRight.y - 100.dp.toPx())
                    lineTo(rect.topRight.x, rect.topRight.y)
                }

                drawIntoCanvas { canvas ->
                    canvas.drawOutline(
                        outline = Outline.Generic(path),
                        paint = Paint().apply {
                            color = Gray5
                            pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                        }
                    )
                    canvas.drawRect(
                        fillRect,
                        paint = Paint().apply {
                            color = Gray5
                        }
                    )
                }
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Box(
                modifier = Modifier.padding(bottom = 60.dp).size(150.dp)
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
