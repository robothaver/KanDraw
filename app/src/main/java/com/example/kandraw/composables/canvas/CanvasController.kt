package com.example.kandraw.composables.canvas

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import com.example.kandraw.viewModel.PenSettings
import kotlin.math.pow

class CanvasController(
    private val allPaths: SnapshotStateList<PathData>,
    val undoPaths: MutableList<PathData>,
    val redoPaths: MutableList<PathData>,
) {

    fun getAllPaths(): List<PathData> {
        return allPaths.toList()
    }

    fun addNewPath(offset: Offset, penSettings: PenSettings) {
        println("ASDASDSDADSASDA ${penSettings.strokeWidth}")
        allPaths.add(
            PathData(
                points = mutableListOf(offset),
                alpha = penSettings.alpha,
                color = penSettings.penColor.color,
                style = penSettings.style,
                cap = penSettings.cap,
                strokeWidth = penSettings.strokeWidth
            )
        )
        if (undoPaths.size == 63) {
            undoPaths.removeFirst()
        }
        undoPaths.add(allPaths.last())
        redoPaths.clear()
    }

    fun expandPath(newPoint: Offset) {
        val newPoints = mutableListOf<Offset>()
        newPoints.addAll(allPaths.last().points)
        newPoints.add(newPoint)
        allPaths[allPaths.lastIndex] =
            allPaths[allPaths.lastIndex].copy(points = newPoints)
        undoPaths[undoPaths.lastIndex] = allPaths.last()
    }

    fun eraseSelectedPath(currentlySelectedPosition: Offset) {
        val pathIndex = getSelectedPathIndex(allPaths, currentlySelectedPosition, 100f)
        if (pathIndex != null) {
            if (undoPaths.size == 63) {
                undoPaths.removeFirst()
            }
            undoPaths.add(allPaths[pathIndex].copy(wasErased = true, index = pathIndex))
            redoPaths.clear()
            allPaths.removeAt(pathIndex)
        }
    }

    fun undo() {
        if (undoPaths.isNotEmpty()) {
            println(undoPaths.last())
            redoPaths.add(undoPaths.last())
            if (undoPaths.last().wasErased) {
                allPaths.add(undoPaths.last().index, undoPaths.last())
            } else {
                allPaths.removeLast()
            }
            undoPaths.removeLast()
        }
    }

    fun redo() {
        if (redoPaths.isNotEmpty()) {
            println(redoPaths.last())
            undoPaths.add(redoPaths.last())
            if (redoPaths.last().wasErased) {
                allPaths.remove(allPaths.find { it.points == redoPaths.last().points })
            } else {
                allPaths.add(redoPaths.last())
            }
            redoPaths.removeLast()
        }
    }

    private fun getSelectedPathIndex(
        allPath: SnapshotStateList<PathData>,
        circle: Offset,
        radius: Float
    ): Int? {
        for (index in allPath.lastIndex downTo 0) {
            for (point in allPath[index].points) {
                if (isInside(
                        circle.x,
                        circle.y,
                        radius,
                        point.x,
                        point.y
                    )
                ) {
                    return index
                }
            }
        }
        return null
    }

    private fun isInside(
        circleX: Float,
        circleY: Float,
        radius: Float,
        x: Float,
        y: Float
    ): Boolean {
        return (circleX - x).pow(2) + (circleY - y).pow(2) <= radius.pow(2)
    }


}