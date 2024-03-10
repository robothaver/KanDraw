package com.example.kandraw.domain.canvasController

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import com.example.kandraw.utils.penEffect.getPenEffect
import com.example.kandraw.viewModel.CanvasViewModel
import com.example.kandraw.viewModel.PathData
import kotlin.math.absoluteValue


class CanvasController(
    canvasViewModel: CanvasViewModel
) {
    private val maxUndoSteps = 64
    private val undoPaths = canvasViewModel.undoPaths
    private val redoPaths = canvasViewModel.redoPaths
    val allPaths = canvasViewModel.allPaths
    val visiblePaths = canvasViewModel.visiblePaths
    val penSettings = canvasViewModel.penSettings
    val backgroundColor = canvasViewModel.backgroundColor

    fun addNewPath(offset: Offset, isSinglePoint: Boolean = false) {
        val newPath = Path()
        newPath.moveTo(offset.x, offset.y)
        if (isSinglePoint) newPath.lineTo(offset.x, offset.y)
        val newPathData = PathData(
            path = newPath,
            points = mutableListOf(offset),
            color = penSettings.value.penColor.color,
            cap = penSettings.value.cap,
            strokeWidth = penSettings.value.strokeWidth,
            alpha = penSettings.value.alpha,
            style = getPenEffect(penSettings.value)
        )
        allPaths.add(newPathData)
        visiblePaths.add(newPathData)
        addUndoStep(newPathData)
        redoPaths.clear()
    }

    fun expandPath(newPoint: Offset) {
        if (checkDistance(newPoint)) {
            allPaths.last().path.lineTo(newPoint.x, newPoint.y)
        }
        visiblePaths[visiblePaths.lastIndex] =
            visiblePaths.last().copy(points = getNewPoints(newPoint))
        allPaths[allPaths.lastIndex] = visiblePaths.last()
        undoPaths[undoPaths.lastIndex] = visiblePaths.last()
    }

    fun eraseSelectedPath(currentlySelectedPosition: Offset, eraserWidth: Float) {
        val selectedPath = getSelectedPath(currentlySelectedPosition, eraserWidth)
        if (selectedPath != null) {
            addUndoStep(
                selectedPath.copy(
                    wasErased = true,
                    index = visiblePaths.indexOf(selectedPath)
                )
            )
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

    private fun getNewPoints(newPoint: Offset): MutableList<Offset> {
        val newPoints = mutableListOf<Offset>()
        newPoints.addAll(allPaths.last().points)
        newPoints.add(newPoint)
        return newPoints
    }

    private fun checkDistance(newPoint: Offset, threshold: Float = 10f): Boolean {
        val previousPoint = allPaths.last().points.last()
        if (penSettings.value.cap != StrokeCap.Round && allPaths.last().points.size < 10) {
            val difference = previousPoint - newPoint
            return (difference.x.absoluteValue >= threshold && difference.y.absoluteValue >= threshold)
        }
        return true
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
                    val collision = PathIntersectionChecker().lineCircle(
                        currentPoint.x,
                        currentPoint.y,
                        nextPoint.x,
                        nextPoint.y,
                        circle.x,
                        circle.y,
                        path.strokeWidth / 2 + radius
                    )
                    if (collision) return path
                } else {
                    if (PathIntersectionChecker().pointCircle(
                            circle.x,
                            circle.y,
                            currentPoint.x,
                            currentPoint.y,
                            path.strokeWidth / 2 + radius
                        )
                    ) {
                        return path
                    }
                }
            }
        }
        return null
    }
}