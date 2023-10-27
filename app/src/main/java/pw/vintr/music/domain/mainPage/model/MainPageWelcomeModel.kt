package pw.vintr.music.domain.mainPage.model

import androidx.annotation.StringRes
import pw.vintr.music.R
import java.util.Calendar

enum class MainPageWelcomeModel(
    @StringRes
    val textRes: Int,
) {
    MORNING(
        textRes = R.string.welcome_title_morning,
    ),
    DAY(
        textRes = R.string.welcome_title_day,
    ),
    EVENING(
        textRes = R.string.welcome_title_evening,
    );

    companion object {
        fun getByNowTime(): MainPageWelcomeModel = when (
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        ) {
            in 6..11 -> MORNING
            in 12..17 -> DAY
            else -> EVENING
        }
    }
}
