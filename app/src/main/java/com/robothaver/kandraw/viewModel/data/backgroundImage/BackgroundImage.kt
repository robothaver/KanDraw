package com.robothaver.kandraw.viewModel.data.backgroundImage

import android.graphics.Bitmap

data class BackgroundImage(
    val image: Bitmap? = null,
    val originalImage: Bitmap? = null,
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
