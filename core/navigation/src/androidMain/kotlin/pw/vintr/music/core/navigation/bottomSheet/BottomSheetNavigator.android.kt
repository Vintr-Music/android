//noinspection UsingMaterialAndMaterial3Libraries
package pw.vintr.music.core.navigation.bottomSheet

import androidx.compose.material.ModalBottomSheetState
import androidx.navigation.Navigator

@Navigator.Name(BottomSheetNavigator.NAME)
class AndroidBottomSheetNavigator(
    sheetState: ModalBottomSheetState
) : BottomSheetNavigator(sheetState)

actual fun createBottomSheetNavigator(
    sheetState: ModalBottomSheetState
): BottomSheetNavigator = AndroidBottomSheetNavigator(sheetState)
