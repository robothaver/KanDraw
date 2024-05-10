package com.robothaver.kandraw.dialogs.penSettingsDialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.robothaver.kandraw.composables.customColorPicker.CustomColorPicker
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.dialogs.getDialogSize
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.HorizontalLayout
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.VerticalLayout
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.updateColor
import com.robothaver.kandraw.viewModel.data.PenSettings
import com.robothaver.kandraw.viewModel.data.Tools
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.utils.windowInfo.WindowType

@Composable
fun PenSettingsDialog(
    selectedDialog: MutableState<Dialogs>,
    penSettings: MutableState<PenSettings>,
    activeTool: MutableState<Tools>,
    windowInfo: WindowInfo
) {
    val isSelectingCustomColor = remember { mutableStateOf(false) }
    val size = getDialogSize(windowInfo.screenWidthInfo, windowInfo.screenHeightInfo)
    AnimatedContent(
        targetState = isSelectingCustomColor.value,
        label = "penSettingsDialogPageChange"
    ) { selectingCustomColor ->
        Column(modifier = Modifier
            .fillMaxWidth(size.width)
            .fillMaxHeight(size.height)
        ) {
            if (!selectingCustomColor) {
                if (windowInfo.screenWidthInfo == WindowType.Compact) {
                    VerticalLayout(
                        penSettings = penSettings, isColorPickingPage = isSelectingCustomColor
                    )
                } else {
                    HorizontalLayout(
                        penSettings = penSettings,
                        isColorPickingPage = isSelectingCustomColor
                    )
                }
            } else {
                CustomColorPicker(
                    initialColor = penSettings.value.customColor,
                    penColor = penSettings.value.penColor,
                    onDismiss = { isSelectingCustomColor.value = false },
                    layout = windowInfo.screenWidthInfo,
                    onBrightnessChanged = { updateColor(penSettings, brightness = it) },
                    onColorPickerActivated = {
                        activeTool.value = Tools.ColorPicker
                        selectedDialog.value = Dialogs.None
                    },
                ) {
                    updateColor(penSettings, it)
                    penSettings.value = penSettings.value.copy(customColor = it)
                }
            }
        }
    }
}
