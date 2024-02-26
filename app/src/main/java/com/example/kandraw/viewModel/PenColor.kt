package com.example.kandraw.viewModel

import androidx.compose.ui.graphics.Color

data class PenColor(
    val hue: Color,
    val brightness: Float = 1f,
    val color: Color
)
