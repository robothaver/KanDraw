package com.robothaver.kandraw.composables.customColorPicker.utils

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.viewModel.data.PenSettings

fun setColorToCustom(
    penSettings: MutableState<PenSettings>,
    customColor: Color,
    fromColorPicker: Boolean = false
) {
    val isInGrayScale = inGrayScale(customColor)
    val brightness = if (isInGrayScale) {
        getColorBrightness(customColor)
    } else if (fromColorPicker) 1f
    else penSettings.value.penColor.brightness

    penSettings.value = penSettings.value.copy(
        penColor = penSettings.value.penColor.copy(
            color = if (isInGrayScale) customColor else changeColorBrightness(
                customColor,
                brightness
            ),
            hue = if (isInGrayScale) getPeakColorHue(customColor) else customColor,
            brightness = brightness
        ),
    )
}

fun inGrayScale(color: Color): Boolean {
    return color.red == color.green && color.red == color.blue
}