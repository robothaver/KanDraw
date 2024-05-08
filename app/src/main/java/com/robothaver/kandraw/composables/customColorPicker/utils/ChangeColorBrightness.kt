package com.robothaver.kandraw.composables.customColorPicker.utils

import androidx.compose.ui.graphics.Color

fun changeColorBrightness(color: Color, brightness: Float): Color {
    return Color(
        color.red * brightness,
        color.green * brightness,
        color.blue * brightness
    )
}