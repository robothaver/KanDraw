package com.example.kandraw.composables.canvas

import android.graphics.Paint
import android.graphics.Paint.Style
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.composables.canvas.canvasCore.CanvasEventHandler
import com.example.kandraw.composables.canvas.canvasCore.getPathsToDraw
import com.example.kandraw.composables.canvas.composables.ColorPickerTool
import com.example.kandraw.composables.canvas.composables.Eraser
import com.example.kandraw.domain.canvasController.CanvasController
import com.example.kandraw.viewModel.Tools


@Composable
fun MainCanvas(
    backgroundColor: Color,
    activeTool: MutableState<Tools>,
    viewPortPosition: MutableState<Offset>,
    canvasController: CanvasController
) {
    val selectedPosition = remember { mutableStateOf(Offset(0f, 0f)) }
    val isTouchEventActive = remember { mutableStateOf(false) }
    val canvasEventHandler = CanvasEventHandler(
        selectedPosition,
        isTouchEventActive,
        activeTool,
        canvasController,
        viewPortPosition
    )

    Box(modifier = Modifier
        .background(backgroundColor)
        .fillMaxSize()
        .clipToBounds()
        .pointerInput(false) {
            detectDragGestures(
                onDragStart = { offset ->
                    canvasEventHandler.dragStart(offset)
                },
                onDrag = { change, offset ->
                    canvasEventHandler.drag(change.position, offset)
                },
                onDragEnd = {
                    canvasEventHandler.dragEnd()
                }
            )
        }
        .pointerInput(true) {
            detectTapGestures { offset ->
                canvasEventHandler.tap(offset)
            }
        }
        .drawBehind {
            translate(left = viewPortPosition.value.x, top = viewPortPosition.value.y) {
                val pathsToDraw = getPathsToDraw(
                    activeTool, canvasController, viewPortPosition, size
                )
                pathsToDraw.forEach { path ->
                    drawPath(
                        path = path.path,
                        color = path.color,
                        style = Stroke(
                            width = path.strokeWidth,
                            cap = path.cap,
                            join = StrokeJoin.Round,
                            miter = 100f,
                            pathEffect = path.style
                        ),
                        alpha = path.alpha
                    )
//                    drawPoints(path.points, pointMode = PointMode.Points, Color.Red, path.strokeWidth)
                }

            }
        }
    ) {
        if (isTouchEventActive.value && activeTool.value == Tools.ColorPicker) {
            ColorPickerTool(canvasController = canvasController, selectedPosition.value)
        } else if (isTouchEventActive.value && activeTool.value == Tools.Eraser) {
            Eraser(selectedPosition = selectedPosition)
        }
        Text(text = "Paths rendered: ${canvasController.visiblePaths.size}", fontSize = 22.sp)
    }
}

@Composable
fun test() {
//    Star
//    val path = Path().apply {
//        lineTo(25f, 55f,)
//        lineTo(75f, 65f,)
//        lineTo(30f, 90f,)
//        lineTo(45f, 145f,)
//        lineTo(0f, 110f,)
//        lineTo(-45f, 145f,)
//        lineTo(-30f, 90f,)
//        lineTo(-75f, 65f,)
//        lineTo(-25f, 55f,)
//        lineTo(0f, 0f,)
//    }

//    Triangle
//    val path = Path().apply {
//        lineTo(25f, 50f)
//        lineTo(-25f, 50f)
//        lineTo(0f, 0f)
//    }

//    Hexagon
//    val path = Path().apply {
//        lineTo(30f, 17f)
//        lineTo(30f, 50f)
//        lineTo(0f, 67f)
//        lineTo(-30f, 50f)
//        lineTo(-30f, 17f)
//    }

//    Arrow
//    val path = Path().apply {
//        lineTo(30f, 20f)
//        lineTo(20f, 30f)
//        lineTo(0f, 17f)
//        lineTo(-20f, 30f)
//        lineTo(-30f, 20f)
//    }
//    Canvas(modifier = Modifier.size(128.dp).background(Color.DarkGray)) {
//        translate(left = size.width / 2, top = size.height / 2) {
//            drawPath(
//                path,
//                Color.Red,
//            )
//        }
//    }
//    Text(text = "ASDASUIDHASKDJHASK")
}