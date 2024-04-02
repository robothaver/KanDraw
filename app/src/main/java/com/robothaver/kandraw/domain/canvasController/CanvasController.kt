package com.robothaver.kandraw.domain.canvasController

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.robothaver.kandraw.utils.penEffect.getPenEffect
import com.robothaver.kandraw.viewModel.Actions
import com.robothaver.kandraw.viewModel.CanvasViewModel
import com.robothaver.kandraw.viewModel.PathData
import kotlin.math.pow


class CanvasController(
    canvasViewModel: CanvasViewModel
) {
    private val maxUndoSteps = 64
    private val undoPaths = canvasViewModel.undoPaths
    private val redoPaths = canvasViewModel.redoPaths
    private val allPathBackup = canvasViewModel.allPathBackup
    val allPaths = canvasViewModel.allPaths
    val visiblePaths = mutableListOf<PathData>()
    val penSettings = canvasViewModel.penSettings
    val backgroundColor = canvasViewModel.backgroundColor
    val eraserWidth = canvasViewModel.eraserWidth

    fun addNewPath(offset: Offset) {
        val newPath = Path()
        newPath.moveTo(offset.x, offset.y)
        newPath.lineTo(offset.x, offset.y)
        val newPathData = PathData(
            path = newPath,
            points = listOf(offset),
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
            visiblePaths[visiblePaths.lastIndex] =
                visiblePaths.last().copy(points = getNewPoints(newPoint))
            allPaths[allPaths.lastIndex] = visiblePaths.last()
            undoPaths[undoPaths.lastIndex] = visiblePaths.last()
        }
    }

    fun eraseSelectedPath(currentlySelectedPosition: Offset) {
        val selectedPath =
            getSelectedPath(currentlySelectedPosition, eraserWidth.floatValue * 1.75f)
        if (selectedPath != null) {
            addUndoStep(
                selectedPath.copy(
                    action = Actions.Erase, index = visiblePaths.indexOf(selectedPath)
                )
            )
            redoPaths.clear()
            allPaths.remove(selectedPath)
            visiblePaths.remove(selectedPath)
        }
    }

    fun clearCanvas() {
        if (allPaths.isNotEmpty()) {
            val newBackup = mutableListOf<PathData>()
            newBackup.addAll(allPaths)
            allPathBackup.addAll(listOf(newBackup))
            addUndoStep(
                allPaths.last().copy(action = Actions.Clear, index = allPathBackup.lastIndex)
            )
            visiblePaths.clear()
            allPaths.clear()
            redoPaths.clear()
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
            if (undoPaths.last().action == Actions.Clear && allPathBackup.isNotEmpty()) {
                try {
                    allPaths.addAll(allPathBackup[undoPaths.last().index])
                } catch (e: IndexOutOfBoundsException) {
                    allPaths.addAll(allPathBackup[undoPaths.last().index - 1])
                }
            } else if (undoPaths.last().action == Actions.Erase) {
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
            if (redoPaths.last().action == Actions.Clear && allPathBackup.isNotEmpty()) {
                try {
                    allPaths.removeAll(allPathBackup[redoPaths.last().index])
                    visiblePaths.removeAll(allPathBackup[redoPaths.last().index])
                } catch (e: IndexOutOfBoundsException) {
                    allPaths.removeAll(allPathBackup.last())
                    visiblePaths.removeAll(allPathBackup.last())
                }

            } else if (redoPaths.last().action == Actions.Erase) {
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
        val difference =
            ((previousPoint.x - newPoint.x).pow(2) + (previousPoint.y - newPoint.y).pow(2)).pow(0.5f)
        return difference >= threshold
    }

    private fun addUndoStep(path: PathData) {
        if (undoPaths.size == maxUndoSteps) {
            if (undoPaths.first().action == Actions.Clear) {
                try {
                    allPathBackup.removeAt(undoPaths.first().index)
                } catch (_: IndexOutOfBoundsException) {
                    allPathBackup.removeFirst()
                }

                val newDick = mutableListOf<PathData>()
                undoPaths.forEach {
                    if (it.action == Actions.Clear) {
                        newDick.add(it.copy(index = it.index - 1))
                    } else {
                        newDick.add(it)
                    }
                }

                undoPaths.clear()
                undoPaths.addAll(newDick)
            }
            undoPaths.removeFirst()
        }
        undoPaths.add(path)
    }

    private fun getSelectedPath(
        circle: Offset, radius: Float
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