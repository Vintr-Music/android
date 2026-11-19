package pw.vintr.music.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography()

expect val Rubik: FontFamily
expect val Gilroy: FontFamily

val BaseRubikStyle = TextStyle(fontFamily = Rubik)

val RubikRegular = BaseRubikStyle.copy(fontWeight = FontWeight.Normal)
val RubikMedium = BaseRubikStyle.copy(fontWeight = FontWeight.Medium)
val RubikBold = BaseRubikStyle.copy(fontWeight = FontWeight.Bold)

val RubikRegular14 = RubikRegular.copy(fontSize = 14.sp)
val RubikRegular16 = RubikRegular.copy(fontSize = 16.sp)
val RubikMedium14 = RubikMedium.copy(fontSize = 14.sp)
val RubikMedium16 = RubikMedium.copy(fontSize = 16.sp)
val RubikMedium18 = RubikMedium.copy(fontSize = 18.sp)
val RubikBold12 = RubikBold.copy(fontSize = 12.sp)

val BaseGilroyStyle = TextStyle(fontFamily = Gilroy)

val Gilroy10 = BaseGilroyStyle.copy(fontSize = 10.sp)
val Gilroy11 = BaseGilroyStyle.copy(fontSize = 11.sp)
val Gilroy12 = BaseGilroyStyle.copy(fontSize = 12.sp)
val Gilroy14 = BaseGilroyStyle.copy(fontSize = 14.sp)
val Gilroy16 = BaseGilroyStyle.copy(fontSize = 16.sp)
val Gilroy18 = BaseGilroyStyle.copy(fontSize = 18.sp)
val Gilroy20 = BaseGilroyStyle.copy(fontSize = 20.sp)
val Gilroy24 = BaseGilroyStyle.copy(fontSize = 24.sp)
val Gilroy32 = BaseGilroyStyle.copy(fontSize = 32.sp)
val Gilroy36 = BaseGilroyStyle.copy(fontSize = 36.sp)
