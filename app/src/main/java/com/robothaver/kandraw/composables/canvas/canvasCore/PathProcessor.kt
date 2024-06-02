package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.viewModel.data.PathData


class PathProcessor(
    private val allPaths: SnapshotStateList<PathData>,
    private val viewPortPosition: Offset,
    private val size: IntSize
) {
    private fun isVisible(points: List<Offset>, pathSize: Float): Boolean {
        for (point in points) {
            if (point.x + viewPortPosition.x in -pathSize..size.width + pathSize &&
                point.y + viewPortPosition.y in -pathSize..size.height + pathSize
            ) {
                return true
            }
        }
        return false
    }

    fun processPaths(): List<PathData> {
        val pathsToRender = mutableListOf<PathData>()
        for (path in allPaths) {
            if (isVisible(path.points, path.strokeWidth)) {
                pathsToRender.add(path)
            }
        }
        return pathsToRender
    }

}
