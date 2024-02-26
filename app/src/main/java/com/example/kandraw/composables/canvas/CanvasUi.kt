package com.example.kandraw.composables.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import com.example.kandraw.viewModel.PenSettings


@Composable
fun MainCanvas(
    backgroundColor: Color,
    activeTool: MutableState<String>,
    penSettings: MutableState<PenSettings>,
    viewPortPosition: MutableState<Offset>,
    canvasController: CanvasController,
) {
    var isEraserVisible by remember { mutableStateOf(false) }
    val eraserPosition = remember { mutableStateOf(Offset(0f, 0f)) }
    Column {
        Text(text = "Paths rendered: ${canvasController.visiblePaths.size}", fontSize = 22.sp)
        Canvas(modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(false) {
                detectDragGestures(
                    onDragStart = { offset ->
                        if (activeTool.value == "pen") {
                            canvasController.addNewPath(getOffset(viewPortPosition.value, offset))
                        } else if (activeTool.value == "eraser") {
                            isEraserVisible = true
                        }
                    },
                    onDrag = { change, offset ->
                        when (activeTool.value) {
                            "eraser" -> {
                                eraserPosition.value = change.position
                                canvasController.eraseSelectedPath(
                                    getOffset(viewPortPosition.value, change.position), 20f
                                )
                            }

                            "mover" -> {
                                viewPortPosition.value += offset
                            }

                            else -> {
                                val newPoint = change.position - offset
                                canvasController.expandPath(getOffset(viewPortPosition.value, newPoint))
                            }
                        }
                    },
                    onDragEnd = {
                        if (activeTool.value == "eraser") isEraserVisible = false
                    }
                )
            }
            .pointerInput(true) {
                detectTapGestures { offset ->
                    if (activeTool.value == "eraser") {
                        eraserPosition.value = offset
                        canvasController.eraseSelectedPath(
                            getOffset(viewPortPosition.value, offset), 20f
                        )
                    } else if (activeTool.value != "mover") {
                        canvasController.addNewPath(
                            getOffset(viewPortPosition.value, offset)
                        )
                    }
                }
            }
        ) {
            translate(left = viewPortPosition.value.x, top = viewPortPosition.value.y) {
                val pathsToDraw: List<PathData>
                if (activeTool.value == "mover") {
                    val newPathProcessor = PathProcessor(canvasController.allPaths, viewPortPosition.value, size)
                    pathsToDraw = newPathProcessor.processPathsTest()
                    canvasController.addVisiblePaths(pathsToDraw)
                } else {
                    pathsToDraw = canvasController.visiblePaths
                }
                pathsToDraw.forEach { path ->
                    drawPath(
                        path = path.path,
                        color = path.color,
                        style = Stroke(
                            width = path.strokeWidth,
                            cap = path.cap,
                            join = StrokeJoin.Round
                        ),
                        alpha = path.alpha
                    )
                }
            }

            if (isEraserVisible) {
                drawCircle(
                    color = penSettings.value.penColor.color,
                    radius = penSettings.value.strokeWidth,
                    center = eraserPosition.value,
                    style = Stroke(width = penSettings.value.strokeWidth),
                    alpha = penSettings.value.alpha
                )
            }

        }

    }
}

private fun getOffset(viewPortPosition: Offset, newPoint: Offset): Offset {
    return Offset(newPoint.x - viewPortPosition.x, newPoint.y - viewPortPosition.y)
}
