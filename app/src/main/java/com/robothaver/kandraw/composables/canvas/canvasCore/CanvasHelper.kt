package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.PathData
import com.robothaver.kandraw.viewModel.data.Tools

fun getPathsToDraw(
    activeTool: MutableState<Tools>,
    canvasController: CanvasController,
    viewPortPosition: MutableState<Offset>,
    size: Size
): List<PathData> {
    val pathsToDraw: List<PathData>
    if (activeTool.value == Tools.Mover || canvasController.visiblePaths.isEmpty()) {
        val pathProcessor = PathProcessor(canvasController.allPaths, viewPortPosition, size)
        pathsToDraw = pathProcessor.processPaths()
        canvasController.addVisiblePaths(pathsToDraw)
    } else {
        pathsToDraw = canvasController.visiblePaths
    }
    return pathsToDraw
}
