package pw.vintr.music.ui.kit.input

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pw.vintr.music.tools.extension.Empty
import pw.vintr.music.ui.theme.Gilroy11
import pw.vintr.music.ui.theme.LocalVintrColors
import pw.vintr.music.ui.theme.RubikRegular16

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String = String.Empty,
    onValueChange: (String) -> Unit = {},
    hint: String = String.Empty,
    label: String = String.Empty,
    inputType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    actionOnDone: ((String) -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val localFocusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = label,
            style = Gilroy11,
            color = LocalVintrColors.current.textFieldLabel
        )
        Spacer(modifier = Modifier.height(14.dp))
        BasicTextField(
            value = value,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    LocalVintrColors.current.textFieldBackground,
                    RoundedCornerShape(6.dp)
                )
                .defaultMinSize(
                    minWidth = 64.dp,
                    minHeight = 40.dp
                ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = RubikRegular16.copy(
                color = LocalVintrColors.current.textButtonContent,
            ),
            cursorBrush = SolidColor(LocalVintrColors.current.textButtonContent),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = inputType
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    actionOnDone?.invoke(value)
                }
            ),
            interactionSource = interactionSource,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            decorationBox = @Composable { innerTextField ->
                Row(
                    modifier = modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                style = RubikRegular16,
                                color = LocalVintrColors.current.textFieldHint,
                            )
                        }

                        innerTextField()
                    }
                }
            }
        )
    }
}