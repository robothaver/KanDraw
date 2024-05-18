package com.robothaver.kandraw.domain.canvasController

import android.graphics.Bitmap
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.viewModel.data.BackgroundImage

class BitmapProcessor() {
    fun resizeBitmap(backgroundImage: BackgroundImage, size: IntSize): Bitmap {
        val scaleRatio = size.width.toFloat() / backgroundImage.image!!.width
        val newHeight = backgroundImage.image.height * scaleRatio
        val newWidth = backgroundImage.image.width * scaleRatio
        println("Image size: ${backgroundImage.image.width} ${backgroundImage.image.height}")
        println("Og size: $size")
        println("Scale: $scaleRatio")
        println("New height: $newHeight")
        println("New width: $newWidth")
        return Bitmap.createScaledBitmap(backgroundImage.image, size.width, size.height, true)
    }
}