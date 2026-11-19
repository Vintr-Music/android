package pw.vintr.music.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.gilroy_extra_bold
import pw.vintr.music.core.ui.resources.rubik_bold
import pw.vintr.music.core.ui.resources.rubik_medium
import pw.vintr.music.core.ui.resources.rubik_regular

val Typography = Typography()

@Immutable
data class VintrTypography(
    val rubikRegular: TextStyle = TextStyle.Default,
    val rubikMedium: TextStyle = TextStyle.Default,
    val rubikBold: TextStyle = TextStyle.Default,
    val rubikRegular14: TextStyle = TextStyle.Default,
    val rubikRegular16: TextStyle = TextStyle.Default,
    val rubikMedium14: TextStyle = TextStyle.Default,
    val rubikMedium16: TextStyle = TextStyle.Default,
    val rubikMedium18: TextStyle = TextStyle.Default,
    val rubikBold12: TextStyle = TextStyle.Default,
    val gilroy10: TextStyle = TextStyle.Default,
    val gilroy11: TextStyle = TextStyle.Default,
    val gilroy12: TextStyle = TextStyle.Default,
    val gilroy14: TextStyle = TextStyle.Default,
    val gilroy16: TextStyle = TextStyle.Default,
    val gilroy18: TextStyle = TextStyle.Default,
    val gilroy20: TextStyle = TextStyle.Default,
    val gilroy24: TextStyle = TextStyle.Default,
    val gilroy32: TextStyle = TextStyle.Default,
    val gilroy36: TextStyle = TextStyle.Default,
)

val LocalVintrTypography = staticCompositionLocalOf { VintrTypography() }

@Composable
fun rememberVintrTypography(): VintrTypography {
    val rubikRegular = Font(Res.font.rubik_regular, FontWeight.Normal)
    val rubikMedium = Font(Res.font.rubik_medium, FontWeight.Medium)
    val rubikBold = Font(Res.font.rubik_bold, FontWeight.Bold)
    val gilroyExtraBold = Font(Res.font.gilroy_extra_bold)

    return remember(rubikRegular, rubikMedium, rubikBold, gilroyExtraBold) {
        val rubik = FontFamily(rubikRegular, rubikMedium, rubikBold)
        val gilroy = FontFamily(gilroyExtraBold)

        val baseRubik = TextStyle(fontFamily = rubik)
        val rubikRegularStyle = baseRubik.copy(fontWeight = FontWeight.Normal)
        val rubikMediumStyle = baseRubik.copy(fontWeight = FontWeight.Medium)
        val rubikBoldStyle = baseRubik.copy(fontWeight = FontWeight.Bold)
        val baseGilroy = TextStyle(fontFamily = gilroy)

        VintrTypography(
            rubikRegular = rubikRegularStyle,
            rubikMedium = rubikMediumStyle,
            rubikBold = rubikBoldStyle,
            rubikRegular14 = rubikRegularStyle.copy(fontSize = 14.sp),
            rubikRegular16 = rubikRegularStyle.copy(fontSize = 16.sp),
            rubikMedium14 = rubikMediumStyle.copy(fontSize = 14.sp),
            rubikMedium16 = rubikMediumStyle.copy(fontSize = 16.sp),
            rubikMedium18 = rubikMediumStyle.copy(fontSize = 18.sp),
            rubikBold12 = rubikBoldStyle.copy(fontSize = 12.sp),
            gilroy10 = baseGilroy.copy(fontSize = 10.sp),
            gilroy11 = baseGilroy.copy(fontSize = 11.sp),
            gilroy12 = baseGilroy.copy(fontSize = 12.sp),
            gilroy14 = baseGilroy.copy(fontSize = 14.sp),
            gilroy16 = baseGilroy.copy(fontSize = 16.sp),
            gilroy18 = baseGilroy.copy(fontSize = 18.sp),
            gilroy20 = baseGilroy.copy(fontSize = 20.sp),
            gilroy24 = baseGilroy.copy(fontSize = 24.sp),
            gilroy32 = baseGilroy.copy(fontSize = 32.sp),
            gilroy36 = baseGilroy.copy(fontSize = 36.sp),
        )
    }
}

val RubikRegular: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikRegular

val RubikMedium: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikMedium

val RubikBold: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikBold

val RubikRegular14: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikRegular14

val RubikRegular16: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikRegular16

val RubikMedium14: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikMedium14

val RubikMedium16: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikMedium16

val RubikMedium18: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikMedium18

val RubikBold12: TextStyle
    @Composable get() = LocalVintrTypography.current.rubikBold12

val Gilroy10: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy10

val Gilroy11: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy11

val Gilroy12: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy12

val Gilroy14: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy14

val Gilroy16: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy16

val Gilroy18: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy18

val Gilroy20: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy20

val Gilroy24: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy24

val Gilroy32: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy32

val Gilroy36: TextStyle
    @Composable get() = LocalVintrTypography.current.gilroy36
