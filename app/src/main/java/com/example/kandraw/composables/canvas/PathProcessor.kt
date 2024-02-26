package com.example.kandraw.composables.canvas

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import java.nio.file.Paths


class PathProcessor(
    private val allPaths: SnapshotStateList<PathData>,
    private val viewPortPosition: Offset,
    private val size: Size
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

    fun processPathsTest(): List<PathData> {
        val pathsToRender = mutableListOf<PathData>()
        for (path in allPaths) {
            if (isVisible(path.points, path.strokeWidth)) {
                pathsToRender.add(path)
            }
        }
        return pathsToRender
    }

}
