package com.example.kandraw.domain.canvasController

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.kandraw.viewModel.PathData
import com.example.kandraw.viewModel.CanvasViewModel


class CanvasController(
    canvasViewModel: CanvasViewModel
) {
    private val maxUndoSteps = 64
    private val undoPaths = canvasViewModel.undoPaths
    private val redoPaths = canvasViewModel.redoPaths
    val allPaths = canvasViewModel.allPaths
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
        val selectedPath = getSelectedPath(currentlySelectedPosition, eraserWidth)
        if (selectedPath != null) {
            addUndoStep(selectedPath.copy(wasErased = true, index = visiblePaths.indexOf(selectedPath)))
            redoPaths.clear()
            allPaths.remove(selectedPath)
            visiblePaths.remove(selectedPath)
        }
    }

    fun getSelectedPathColor(currentlySelectedPosition: Offset): Color? {
        return getSelectedPath(currentlySelectedPosition, 20f)?.color
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

    private fun getSelectedPath(
        circle: Offset,
        radius: Float
    ): PathData? {
        for (path in visiblePaths.reversed()) {
            for (index in 0..path.points.lastIndex) {
                val currentPoint = path.points[index]
                if (index != path.points.lastIndex) {
                    val nextPoint = path.points[index + 1]
                    val collision = PathIntersectionChecker().lineCircle(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, circle.x, circle.y, path.strokeWidth + radius)
                    if (collision) return path
                } else {
                    if (PathIntersectionChecker().pointCircle(circle.x, circle.y, currentPoint.x, currentPoint.y, path.strokeWidth)) {
                        return path
                    }
                }
            }
        }
        return null
    }
}