package com.robothaver.kandraw.viewModel.data.backgroundImage

import android.graphics.Bitmap
import androidx.compose.ui.unit.IntSize

data class BackgroundImage(
    val image: Bitmap? = null,
    val originalSize: IntSize? = null,
    val scaleMode: ScaleModes = ScaleModes.Default,
    val stickToBackground: Boolean = false,
    val isVisible: Boolean = true,
    val alignment: ImageAlignment = ImageAlignments.TopStart
)

enum class ScaleModes {
    Default,
    FillWidth,
    FillHeight,
    StretchToFill,
}
