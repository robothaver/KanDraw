package com.robothaver.kandraw.viewModel.data

import androidx.compose.ui.graphics.Color

data class PenColor(
    val color: Color,
    val hue: Color = color,
    val brightness: Float = 1f
)
