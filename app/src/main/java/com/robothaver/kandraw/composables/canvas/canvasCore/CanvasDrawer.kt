package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.robothaver.kandraw.viewModel.data.GridSettings

class CanvasDrawer(
    private val drawScope: DrawScope,
    gridSettings: GridSettings
) {
    private val isLargeCellEnabled = gridSettings.isLargeCellEnabled
    private val smallCellSize = gridSettings.smallCellSize
    private val largeCellSize = gridSettings.largeCellSize
    private val smallCellColor = gridSettings.smallCellColor.color
    private val largeCellColor = gridSettings.largeCellColor.color
    private val smallCellStrokeWidth = gridSettings.smallCellStrokeWidth
    private val largeCellStrokeWidth = gridSettings.largeCellStrokeWidth
    
    fun drawBackgroundGrid() {
        drawScope.backgroundGrid()
    }

    private fun DrawScope.backgroundGrid() {
        val rows = (size.height / smallCellSize).toInt()
        val columns = (size.width / smallCellSize).toInt()
        val horizontalRange =
            0..rows
        val verticalRange =
            0..columns
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