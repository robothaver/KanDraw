package com.robothaver.kandraw.domain.canvasController

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.get
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage
import com.robothaver.kandraw.viewModel.data.backgroundImage.ScaleModes

class BitmapProcessor {
    fun getColorFromBitmap(backgroundImage: BackgroundImage, offset: Offset): Color? {
        val image = backgroundImage.image
        val isImageValid = image != null && backgroundImage.isVisible

        if (isImageValid && isSelectedPositionValid(IntSize(image!!.width, image.height), offset)
        ) {
            return Color(image[offset.x.toInt(), offset.y.toInt()])
        }
        return null
    }

    private fun isSelectedPositionValid(imageSize: IntSize, selectedPos: Offset): Boolean {
        return selectedPos.x >= 0f && selectedPos.x <= imageSize.width &&
                selectedPos.y >= 0f && selectedPos.y <= imageSize.height
    }

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