package com.robothaver.kandraw.dialogs.penSettingsDialog.utils

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.composables.customColorPicker.utils.changeColorBrightness
import com.robothaver.kandraw.viewModel.data.PenEffectSettings
import com.robothaver.kandraw.viewModel.data.PenSettings

fun updateEffect(penSettings: MutableState<PenSettings>, updateSettings: PenEffectSettings) {
    penSettings.value = penSettings.value.copy(penEffectSettings = updateSettings)
}

fun updateColor(
    penSettings: MutableState<PenSettings>,
    newHue: Color = penSettings.value.penColor.hue,
    brightness: Float = penSettings.value.penColor.brightness
) {
    penSettings.value = penSettings.value.copy(
        penColor = penSettings.value.penColor.copy(
            color = changeColorBrightness(newHue, brightness),
            hue = newHue,
            brightness = brightness
        )
    )
}