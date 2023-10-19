package pw.vintr.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import pw.vintr.music.ui.kit.button.ButtonRegular
import pw.vintr.music.ui.kit.input.AppTextField
import pw.vintr.music.ui.theme.VintrMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            VintrMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .systemBarsPadding()
                            .padding(horizontal = 20.dp)
                    ) {
                        AppTextField(
                            label = "Email",
                            hint = "Email"
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        AppTextField(
                            label = "Пароль",
                            hint = "Пароль"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        ButtonRegular(
                            text = "Войти",
                            onClick = { },
                        )
                        Spacer(modifier = Modifier.height(68.dp))
                    }
                }
            }
        }
    }
}
