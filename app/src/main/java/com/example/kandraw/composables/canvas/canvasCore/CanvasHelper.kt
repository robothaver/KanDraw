package com.example.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kandraw.domain.canvasController.CanvasController
import com.example.kandraw.viewModel.PathData
import com.example.kandraw.viewModel.Tools

fun getPathsToDraw(
    activeTool: MutableState<Tools>,
    canvasController: CanvasController,
    viewPortPosition: MutableState<Offset>,
    size: Size
): List<PathData> {
    val pathsToDraw: List<PathData>
    if (activeTool.value == Tools.Mover) {
        val pathProcessor = PathProcessor(canvasController.allPaths, viewPortPosition, size)
        pathsToDraw = pathProcessor.processPaths()
        canvasController.addVisiblePaths(pathsToDraw)
    } else {
        pathsToDraw = canvasController.visiblePaths
    }
    return pathsToDraw
}
