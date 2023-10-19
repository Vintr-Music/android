package pw.vintr.music.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pw.vintr.music.R

// Default empty typography
val Typography = Typography()

val Rubik = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_medium, FontWeight.Medium),
    Font(R.font.rubik_bold, FontWeight.Bold),
)
val Gilroy = FontFamily(
    Font(R.font.gilroy_extra_bold),
)

val BaseRubikStyle = TextStyle(fontFamily = Rubik)

val RubikRegular = BaseRubikStyle.copy(fontWeight = FontWeight.Normal)
val RubikMedium = BaseRubikStyle.copy(fontWeight = FontWeight.Medium)
val RubikBold = BaseRubikStyle.copy(fontWeight = FontWeight.Bold)

val RubikMedium16 = RubikMedium.copy(fontSize = 16.sp)

val BaseGilroyStyle = TextStyle(fontFamily = Gilroy)
