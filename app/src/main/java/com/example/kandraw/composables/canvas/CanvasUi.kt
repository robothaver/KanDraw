package com.example.kandraw.composables.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import com.example.kandraw.viewModel.PenSettings


@Composable
fun MainCanvas(
    backgroundColor: Color,
    activeTool: MutableState<String>,
    penSettings: MutableState<PenSettings>,
    viewPortPosition: MutableState<Offset>,
    canvasController: CanvasController,
) {
    val isEraserVisible = remember { mutableStateOf(false) }
    val eraserPosition = remember { mutableStateOf(Offset(0f, 0f)) }

    Canvas(modifier = Modifier
        .background(backgroundColor)
        .fillMaxSize()
        .clipToBounds()
        .pointerInput(true) {
            detectDragGestures(
                onDragStart = { offset ->
                    if (activeTool.value == "pen") canvasController.addNewPath(
                        getOffset(
                            viewPortPosition.value,
                            offset
                        ),
                        penSettings.value
                    ) else if (activeTool.value == "eraser") {
                        isEraserVisible.value = true
                    }
                },
                onDrag = { change, offset ->
                    println(size)
                    when (activeTool.value) {
                        "eraser" -> {
                            eraserPosition.value = change.position
                            canvasController.eraseSelectedPath(
                                getOffset(
                                    viewPortPosition.value,
                                    change.position
                                )
                            )
                        }

                        "mover" -> {
                            viewPortPosition.value += offset
                        }

                        else -> {
                            val newPoint = (change.position - offset)
                            canvasController.expandPath(
                                getOffset(
                                    viewPortPosition.value,
                                    newPoint
                                )
                            )
                        }
                    }
                },
                onDragEnd = { if (activeTool.value == "eraser") isEraserVisible.value = false }
            )
        }
        .pointerInput(true) {
            detectTapGestures { offset ->
                if (activeTool.value == "eraser") {
                    eraserPosition.value = offset
                    canvasController.eraseSelectedPath(
                        getOffset(
                            viewPortPosition.value,
                            offset
                        )
                    )
                } else if (activeTool.value != "mover") {
                    println(offset)
                    canvasController.addNewPath(
                        getOffset(viewPortPosition.value, offset),
                        penSettings.value
                    )
                }
            }
        }
    ) {
        translate(left = viewPortPosition.value.x, top = viewPortPosition.value.y) {
            canvasController.getAllPaths().forEach { path ->
                val newPath = processPaths(path.points, viewPortPosition.value, size)
                drawPath(
                    newPath,
                    color = path.color,
                    style = path.style,
                    alpha = path.alpha,
                )
            }
        }
        if (activeTool.value == "eraser" && isEraserVisible.value) {
            drawCircle(
                color = penSettings.value.color,
                radius = penSettings.value.strokeWidth,
                center = eraserPosition.value,
                style = Stroke(width = penSettings.value.strokeWidth),
                alpha = penSettings.value.alpha
            )
        }
    }
}

fun getOffset(viewPortPosition: Offset, newPoint: Offset): Offset {
    return Offset(newPoint.x - viewPortPosition.x, newPoint.y - viewPortPosition.y)
}
