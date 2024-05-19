package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.viewModel.data.GridSettings
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage
import com.robothaver.kandraw.viewModel.data.backgroundImage.ImageAlignments

class CanvasDrawer(
    gridSettings: GridSettings,
    private val drawScope: DrawScope,
    private val backgroundImage: BackgroundImage,
    private val viewportOffset: Offset
) {
    private val isGridEnabled = gridSettings.isGridEnabled
    private val isLargeCellEnabled = gridSettings.isLargeCellEnabled
    private val smallCellSize = gridSettings.smallCellSize
    private val largeCellSize = gridSettings.largeCellSize
    private val smallCellColor = gridSettings.smallCellColor.color
    private val largeCellColor = gridSettings.largeCellColor.color
    private val smallCellStrokeWidth = gridSettings.smallCellStrokeWidth
    private val largeCellStrokeWidth = gridSettings.largeCellStrokeWidth

    fun drawBackgroundImage() {
        if (backgroundImage.image == null || !backgroundImage.isVisible) return
        drawScope.backgroundImage()
    }

    private fun DrawScope.backgroundImage() {
        val offset = getBackgroundImageOffset()
        translate(left = offset.x, top = offset.y) {
            drawImage(backgroundImage.image!!.asImageBitmap())
        }
    }

    private fun getBackgroundImageOffset(): Offset {
        val imageSize = IntSize(backgroundImage.image!!.width, backgroundImage.image.height)
        val centerHorizontally = drawScope.size.width / 2f - imageSize.width / 2
        val centerVertically = drawScope.size.height / 2f - imageSize.height / 2

        val offset = when (backgroundImage.alignment) {
            ImageAlignments.TopCenter -> Offset(centerHorizontally, 0f)
            ImageAlignments.TopEnd -> Offset(drawScope.size.width - imageSize.width, 0f)

            ImageAlignments.CenterStart -> Offset(0f, centerVertically)
            ImageAlignments.Center -> Offset(centerHorizontally, centerVertically)
            ImageAlignments.CenterEnd -> Offset(drawScope.size.width - imageSize.width, centerVertically)

            ImageAlignments.BottomStart -> Offset(0f, drawScope.size.height - imageSize.height)
            ImageAlignments.BottomCenter -> Offset(
                centerHorizontally,
                drawScope.size.height - imageSize.height
            )

            ImageAlignments.BottomEnd -> Offset(
                drawScope.size.width - imageSize.width,
                drawScope.size.height - imageSize.height
            )

            else -> Offset(0f, 0f)
        }
        if (!backgroundImage.stickToBackground) {
            return Offset(offset.x + viewportOffset.x, offset.y + viewportOffset.y)
        }
        return offset
    }

    fun drawBackgroundGrid() {
        if (!isGridEnabled) return
        drawScope.backgroundGrid()
    }

    private fun DrawScope.backgroundGrid() {
        val rows = (size.height / smallCellSize).toInt()
        val columns = (size.width / smallCellSize).toInt()
        val horizontalRange = 0..rows
        val verticalRange = 0..columns
        drawGridLines(
            horizontalRange,
            verticalRange,
            smallCellColor,
            smallCellSize,
            smallCellStrokeWidth
        ) {
            it % largeCellSize != 0 || !isLargeCellEnabled
        }
        if (isLargeCellEnabled) {
            drawGridLines(
                horizontalRange,
                verticalRange,
                largeCellColor,
                smallCellSize,
                largeCellStrokeWidth
            ) {
                it % largeCellSize == 0
            }
        }
    }

    private fun DrawScope.drawGridLines(
        horizontalRange: IntRange,
        verticalRange: IntRange,
        cellColor: Color,
        cellSize: Float,
        strokeWidth: Float,
        whenTrue: (i: Int) -> Boolean
    ) {
        drawHorizontalGridLines(
            horizontalRange,
            cellColor,
            cellSize,
            strokeWidth,
            whenTrue
        )
        drawVerticalGridLines(
            verticalRange,
            cellColor,
            cellSize,
            strokeWidth,
            whenTrue
        )
    }

    private fun DrawScope.drawHorizontalGridLines(
        range: IntRange,
        cellColor: Color,
        cellSize: Float,
        strokeWidth: Float,
        whenTrue: (i: Int) -> Boolean
    ) {
        for (i in range) {
            if (whenTrue(i)) {
                drawLine(
                    color = cellColor,
                    start = Offset(0f, i * cellSize),
                    end = Offset(size.width, i * cellSize),
                    strokeWidth = strokeWidth
                )
            }
        }
    }

    private fun DrawScope.drawVerticalGridLines(
        range: IntRange,
        cellColor: Color,
        cellSize: Float,
        strokeWidth: Float,
        whenTrue: (i: Int) -> Boolean
    ) {
        for (i in range) {
            if (whenTrue(i)) {
                drawLine(
                    color = cellColor,
                    start = Offset(i * cellSize, 0f),
                    end = Offset(i * cellSize, size.height),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}