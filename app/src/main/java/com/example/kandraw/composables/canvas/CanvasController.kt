package com.example.kandraw.composables.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.example.kandraw.viewModel.CanvasViewModel
import kotlin.math.pow

class CanvasController(
    canvasViewModel: CanvasViewModel
) {
    private val maxUndoSteps = 64
    val allPaths = canvasViewModel.allPaths
    val undoPaths = canvasViewModel.undoPaths
    val redoPaths = canvasViewModel.redoPaths
    val visiblePaths = canvasViewModel.visiblePaths
    val penSettings = canvasViewModel.penSettings

    fun addNewPath(offset: Offset) {
        val newPath = Path()
        newPath.moveTo(offset.x, offset.y)
        newPath.lineTo(offset.x, offset.y)
        val newPathData = PathData(
            path = newPath,
            points = mutableListOf(offset),
            color = penSettings.value.penColor.color,
            cap = penSettings.value.cap,
            strokeWidth = penSettings.value.strokeWidth,
            style = penSettings.value.style,
            alpha = penSettings.value.alpha
        )
        allPaths.add(newPathData)
        visiblePaths.add(newPathData)
        addUndoStep(newPathData)
        redoPaths.clear()
    }

    fun expandPath(newPoint: Offset) {
        val newPoints = mutableListOf<Offset>()
        newPoints.addAll(allPaths.last().points)
        newPoints.add(newPoint)
        val currentPath = allPaths.last().path
        currentPath.lineTo(newPoint.x, newPoint.y)
        visiblePaths[visiblePaths.lastIndex] =
            visiblePaths[visiblePaths.lastIndex].copy(points = newPoints, path = currentPath)
        allPaths[allPaths.lastIndex] =
            allPaths[allPaths.lastIndex].copy(points = newPoints, path = currentPath)
        undoPaths[undoPaths.lastIndex] = visiblePaths.last()
    }

    fun eraseSelectedPath(currentlySelectedPosition: Offset, eraserWidth: Float) {
        val pathIndex = getSelectedPathIndex(currentlySelectedPosition, eraserWidth)
        if (pathIndex != null) {
            addUndoStep(visiblePaths[pathIndex].copy(wasErased = true, index = pathIndex))
            redoPaths.clear()
            allPaths.remove(visiblePaths[pathIndex])
            visiblePaths.removeAt(pathIndex)
        }
    }

    fun addVisiblePaths(newVisiblePaths: List<PathData>) {
        visiblePaths.clear()
        visiblePaths.addAll(newVisiblePaths)
    }

    fun undo() {
        if (undoPaths.isNotEmpty()) {
            redoPaths.add(undoPaths.last())
            if (undoPaths.last().wasErased) {
                allPaths.add(undoPaths.last().index, undoPaths.last())
                visiblePaths.add(undoPaths.last().index, undoPaths.last())
            } else {
                allPaths.removeLast()
                visiblePaths.removeLast()
            }
            undoPaths.removeLast()
        }
    }

    fun redo() {
        if (redoPaths.isNotEmpty()) {
            undoPaths.add(redoPaths.last())
            if (redoPaths.last().wasErased) {
                allPaths.removeAt(redoPaths.last().index)
                visiblePaths.removeAt(redoPaths.last().index)
            } else {
                allPaths.add(redoPaths.last())
                visiblePaths.add(redoPaths.last())
            }
            redoPaths.removeLast()
        }
    }

    private fun addUndoStep(path: PathData) {
        if (undoPaths.size == maxUndoSteps) {
            undoPaths.removeFirst()
        }
        undoPaths.add(path)
    }

    private fun getSelectedPathIndex(
        circle: Offset,
        radius: Float
    ): Int? {
        for (index in visiblePaths.lastIndex downTo 0) {
            for (point in visiblePaths[index].points) {
                if (isInside(circle.x, circle.y, radius, point.x, point.y)) {
                    return index
                }
            }
        }
        return null
    }

    private fun isInside(circleX: Float,circleY: Float,radius: Float,x: Float,y: Float): Boolean {
        return (circleX - x).pow(2) + (circleY - y).pow(2) <= radius.pow(2)
    }
}