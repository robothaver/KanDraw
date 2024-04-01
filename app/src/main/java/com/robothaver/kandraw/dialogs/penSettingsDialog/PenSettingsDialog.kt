package com.robothaver.kandraw.dialogs.penSettingsDialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.robothaver.kandraw.composables.CustomColorPicker
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.HorizontalLayout
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.VerticalLayout
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.updateColor
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.viewModel.PenSettings
import com.robothaver.kandraw.viewModel.Tools

@Composable
fun PenSettingsDialog(
    selectedDialog: MutableState<Dialogs>,
    penSettings: MutableState<PenSettings>,
    activeTool: MutableState<Tools>,
    windowInfo: WindowInfo
) {
    val isColorPickingPage = remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isColorPickingPage.value,
        content = { isPickingColor ->
            if (!isPickingColor) {
                if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
                    VerticalLayout(
                        penSettings = penSettings,
                        isColorPickingPage = isColorPickingPage
                    )
                } else {
                    HorizontalLayout(
                        penSettings = penSettings,
                        isColorPickingPage = isColorPickingPage
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(
                            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
                                0.55f
                            } else {
                                0.85f
                            }
                        )
                        .fillMaxWidth(
                            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
                                0.8f
                            } else {
                                0.85f
                            }
                        )
                ) {
                    CustomColorPicker(
                        initialColor = penSettings.value.customColor,
                        penColor = penSettings.value.penColor,
                        onDismiss = { isColorPickingPage.value = false },
                        layout = windowInfo.screenWidthInfo,
                        onBrightnessChanged = { updateColor(penSettings, brightness = it) },
                        onColorPickerActivated = {
                            activeTool.value = Tools.ColorPicker
                            selectedDialog.value = Dialogs.None
                        },
                    ) {
                        updateColor(penSettings, it)
                        penSettings.value = penSettings.value.copy(
                            customColor = it
                        )
                    }
                }
            }
        }, label = "",
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    )
}
