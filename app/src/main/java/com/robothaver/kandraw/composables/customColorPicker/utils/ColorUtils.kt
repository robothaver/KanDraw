package com.robothaver.kandraw.composables.customColorPicker.utils

import androidx.compose.ui.graphics.Color
import java.lang.Math.round
import kotlin.math.max
import kotlin.math.min

fun getColorBrightness(color: Color): Float {
    return max(max(color.red, color.green), color.blue)
}

fun getSaturation(color: Color): Float {
    val r = color.red
    val g = color.green
    val b = color.blue

    val cmax = max(r, max(g, b))
    val cmin = min(r, min(g, b))
    val diff = cmax - cmin // diff of cmax and cmin.

    val saturation = if (cmax == 0f) 0f else diff / cmax * 100

    return saturation / 100
}

fun getHue(color: Color): Int {
    val red = color.red
    val blue = color.blue
    val green = color.green

    val min: Float = min(min(red, green), blue)
    val max: Float = max(max(red, green), blue)

    if (min == max) {
        return 0
    }

    val hue = calculateHue(max, min, red, green, blue) * 60
    return if (hue < 0) {
        round(360 + hue)
    } else {
        round(hue)
    }
}

private fun calculateHue(max: Float, min: Float, red: Float, green: Float, blue: Float): Float {
    return when (max) {
        red -> {
            (green - blue) / (max - min)
        }
        green -> {
            2f + (blue - red) / (max - min)
        }
        else -> {
            4f + (red - green) / (max - min)
        }
    }
}