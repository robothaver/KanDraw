package com.robothaver.kandraw.viewModel.data

import android.graphics.Bitmap
import androidx.compose.ui.unit.IntSize

data class BackgroundImage(
    val image: Bitmap? = null,
    val originalSize: IntSize? = null,
    val scaleMode: ScaleModes = ScaleModes.Default,
    val stickToBackground: Boolean = false,
    val isVisible: Boolean = true
)

enum class ScaleModes {
    Default,
    Fill_width,
    Fill_height,
    Strecth_to_fill,
}
