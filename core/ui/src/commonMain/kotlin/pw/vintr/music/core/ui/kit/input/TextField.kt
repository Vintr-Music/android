package pw.vintr.music.core.ui.kit.input

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pw.vintr.music.core.ui.kit.button.ButtonSimpleIcon
import pw.vintr.music.core.ui.kit.resource.drawablePainter
import pw.vintr.music.core.ui.resources.Res
import pw.vintr.music.core.ui.resources.ic_clear
import pw.vintr.music.core.ui.theme.Gilroy11
import pw.vintr.music.core.ui.theme.LocalVintrColors
import pw.vintr.music.core.ui.theme.RubikRegular16
import pw.vintr.music.core.ui.tools.extension.Empty
import pw.vintr.music.core.ui.tools.extension.clearFocusOnKeyboardDismiss

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    value: String = String.Empty,
    onValueChange: (String) -> Unit = {},
    hint: String = String.Empty,
    label: String = String.Empty,
    leadingIconRes: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    showClearButton: Boolean = false,
    actionOnClear: (() -> Unit) = {},
    actionOnDone: ((String) -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val localFocusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = label,
                style = Gilroy11,
                color = LocalVintrColors.current.textFieldLabel
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
        BasicTextField(
            value = value,
            modifier = textFieldModifier
                .fillMaxWidth()
                .background(
                    LocalVintrColors.current.textFieldBackground,
                    RoundedCornerShape(6.dp)
                )
                .defaultMinSize(
                    minWidth = 64.dp,
                    minHeight = 40.dp
                )
                .clearFocusOnKeyboardDismiss(),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = RubikRegular16.copy(
                color = LocalVintrColors.current.textButtonContent,
            ),
            cursorBrush = SolidColor(LocalVintrColors.current.textButtonContent),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    actionOnDone?.invoke(value)
                },
                onSearch = {
                    localFocusManager.clearFocus()
                    actionOnDone?.invoke(value)
                }
            ),
            interactionSource = interactionSource,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            decorationBox = @Composable { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIconRes?.let { res ->
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = drawablePainter(res),
                            tint = LocalVintrColors.current.textFieldHint,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                style = RubikRegular16,
                                color = LocalVintrColors.current.textFieldHint,
                            )
                        }
                        innerTextField()
                    }

                    if (showClearButton) {
                        Spacer(modifier = Modifier.width(10.dp))
                        ButtonSimpleIcon(
                            icon = Res.drawable.ic_clear,
                            tint = LocalVintrColors.current.textFieldHint,
                            onClick = actionOnClear
                        )
                    }
                }
            }
        )
    }
}
