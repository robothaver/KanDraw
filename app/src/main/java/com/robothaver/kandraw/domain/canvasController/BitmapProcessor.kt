package com.robothaver.kandraw.domain.canvasController

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.get
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage
import com.robothaver.kandraw.viewModel.data.backgroundImage.ScaleModes

class BitmapProcessor {
    fun removeColor(image: Bitmap, backgroundColor: Color) {
        image.eraseColor(backgroundColor.value.toInt())
    }

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

    fun resizeBitmap(bitmap: Bitmap, newSize: IntSize): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap, newSize.width, newSize.height, true
        )
    }

    fun getNewBitmapSize(scaleMode: ScaleModes, screenSize: IntSize, bitmapSize: IntSize): IntSize {
        return when (scaleMode) {
            ScaleModes.Default -> bitmapSize
            ScaleModes.FillWidth -> {
                val scaleRatio = screenSize.width.toFloat() / bitmapSize.width
                IntSize(
                    screenSize.width,
                    (bitmapSize.height * scaleRatio).toInt(),
                )
            }

            ScaleModes.FillHeight -> {
                val scaleRatio = screenSize.height.toFloat() / bitmapSize.height
                IntSize(
                    (bitmapSize.width * scaleRatio).toInt(), screenSize.height
                )
            }

            ScaleModes.StretchToFill -> {
                screenSize
            }
        }
    }

}