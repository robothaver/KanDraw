package com.example.kandraw.dialogs.penSettingsDialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.kandraw.composables.CustomColorPicker
import com.example.kandraw.dialogs.penSettingsDialog.layouts.HorizontalLayout
import com.example.kandraw.dialogs.penSettingsDialog.layouts.VerticalLayout
import com.example.kandraw.dialogs.penSettingsDialog.layouts.updateColor
import com.example.kandraw.utils.windowInfo.WindowInfo
import com.example.kandraw.viewModel.PenSettings
import com.example.kandraw.viewModel.Tools

@Composable
fun PenSettingsDialog(
    isVisible: MutableState<Boolean>,
    penSettings: MutableState<PenSettings>,
    activeTool: MutableState<Tools>,
    windowInfo: WindowInfo,
    windowInsetsController: WindowInsetsControllerCompat
) {
    if (!isVisible.value) return
    val isColorPickingPage = remember { mutableStateOf(false) }

    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    Dialog(
        onDismissRequest = { isVisible.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedContent(
            targetState = isColorPickingPage.value,
            content = { isPickingColor ->
                if (!isPickingColor) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                            .padding(16.dp)
                    ) {
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
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                            .padding(16.dp)
                    ) {
                        CustomColorPicker(
                            initialColor = penSettings.value.customColor,
                            penColor = penSettings.value.penColor,
                            onDismiss = { isColorPickingPage.value = false },
                            layout = windowInfo.screenWidthInfo,
                            onBrightnessChanged = { updateColor(penSettings, brightness = it) },
                            onColorPickerActivated = {
                                activeTool.value = Tools.ColorPicker
                                isVisible.value = false
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
}
