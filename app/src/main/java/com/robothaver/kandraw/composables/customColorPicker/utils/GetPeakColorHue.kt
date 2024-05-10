package com.robothaver.kandraw.composables.customColorPicker.utils

import androidx.compose.ui.graphics.Color

fun getPeakColorHue(color: Color): Color {
    return Color.hsv(getHue(color).toFloat(), getSaturation(color), 1f)
}