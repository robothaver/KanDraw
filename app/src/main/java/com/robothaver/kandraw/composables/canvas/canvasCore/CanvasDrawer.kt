package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate

class CanvasDrawer(
    private val drawScope: DrawScope,
    private val smallCellSize: Float,
    private val largeCellSize: Int,
    private val smallCellColor: Color,
    private val largeCellColor: Color,
    private val smallCellWidth: Float,
    private val largeCellStrokeWidth: Float
) {
    fun drawBackgroundGrid(gridOffset: MutableState<Offset>) {
        drawScope.backgroundGrid(gridOffset)
    }

    private fun DrawScope.backgroundGrid(gridOffset: MutableState<Offset>) {
        val rows = (size.height / smallCellSize).toInt()
        val columns = (size.width / smallCellSize).toInt()
        val horizontalRange =
            -largeCellSize * smallCellSize.toInt()..rows + largeCellSize * smallCellSize.toInt()
        val verticalRange =
            -largeCellSize * smallCellSize.toInt()..columns + largeCellSize * smallCellSize.toInt()

        translate(left = gridOffset.value.x, top = gridOffset.value.y) {
            drawGridLines(
                horizontalRange,
                verticalRange,
                smallCellColor,
                smallCellSize,
                smallCellWidth
            ) {
                it % largeCellSize != 0
            }
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
                    start = Offset(-cellSize * largeCellSize, i * cellSize),
                    end = Offset(size.width + cellSize * largeCellSize, i * cellSize),
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
                    start = Offset(i * cellSize, -cellSize * cellSize),
                    end = Offset(i * cellSize, size.height + cellSize * cellSize),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}