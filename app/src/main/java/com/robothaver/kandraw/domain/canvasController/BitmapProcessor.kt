package com.robothaver.kandraw.domain.canvasController

import android.graphics.Bitmap
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage
import com.robothaver.kandraw.viewModel.data.backgroundImage.ScaleModes

class BitmapProcessor {
    fun resizeBitmap(backgroundImage: BackgroundImage, newSize: IntSize): Bitmap {
        return Bitmap.createScaledBitmap(
            backgroundImage.image!!, newSize.width, newSize.height, true
        )
    }

    fun getNewBitmapSize(backgroundImage: BackgroundImage, screenSize: IntSize): IntSize {
        return when (backgroundImage.scaleMode) {
            ScaleModes.Default -> backgroundImage.originalSize!!
            ScaleModes.FillWidth -> {
                val scaleRatio = screenSize.width.toFloat() / backgroundImage.originalSize!!.width
                IntSize(
                    screenSize.width,
                    (backgroundImage.originalSize.height * scaleRatio).toInt(),
                )
            }

            ScaleModes.FillHeight -> {
                val scaleRatio = screenSize.height.toFloat() / backgroundImage.originalSize!!.height
                IntSize(
                    (backgroundImage.originalSize.width * scaleRatio).toInt(), screenSize.height
                )
            }

            ScaleModes.StretchToFill -> {
                screenSize
            }
        }
    }

}