package com.robothaver.kandraw.composables.canvas

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import com.robothaver.kandraw.composables.canvas.canvasCore.CanvasDrawer
import com.robothaver.kandraw.composables.canvas.canvasCore.CanvasEventHandler
import com.robothaver.kandraw.composables.canvas.canvasCore.getPathsToDraw
import com.robothaver.kandraw.composables.canvas.composables.ColorPickerTool
import com.robothaver.kandraw.composables.canvas.composables.Eraser
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.Tools
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainCanvas(
    backgroundColor: Color,
    activeTool: MutableState<Tools>,
    viewPortPosition: MutableState<Offset>,
    canvasController: CanvasController,
    image: MutableState<Bitmap?>,
    controller: CaptureController,
) {
    val gridSettings = canvasController.gridSettings.value
    val selectedPosition = remember { mutableStateOf(Offset(0f, 0f)) }
    val isTouchEventActive = remember { mutableStateOf(false) }
    val canvasEventHandler = CanvasEventHandler(
        selectedPosition,
        isTouchEventActive,
        activeTool,
        canvasController,
        viewPortPosition,
        canvasController.gridSettings
    )
    Box(modifier = Modifier
        .capturable(controller)
        .background(backgroundColor)
        .fillMaxSize()
        .clipToBounds()
        .pointerInput(false) {
            detectDragGestures(onDragStart = { offset ->
                canvasEventHandler.dragStart(offset)
            }, onDrag = { change, offset ->
                canvasEventHandler.drag(change.position, offset)
            }, onDragEnd = {
                canvasEventHandler.dragEnd()
            })
        }
        .pointerInput(true) {
            detectTapGestures { offset ->
                canvasEventHandler.tap(offset)
            }
        }
        .drawBehind {
            val canvasDrawer = CanvasDrawer(
                this,
                gridSettings.smallCellSize,
                gridSettings.largeCellSize,
                gridSettings.smallCellColor,
                gridSettings.largeCellColor,
                gridSettings.smallCellStrokeWidth,
                gridSettings.largeCellStrokeWidth
            )
            translate(left = viewPortPosition.value.x, top = viewPortPosition.value.y) {
                if (gridSettings.isGridEnabled) {
                    canvasDrawer.drawBackgroundGrid()
                }
                if (image.value != null) {
                    drawImage(
                        image = image.value!!.asImageBitmap()
                    )
                }

                val pathsToDraw = getPathsToDraw(
                    activeTool, canvasController, viewPortPosition, size
                )
                pathsToDraw.forEach { path ->
                    drawPath(
                        path = path.path, color = path.color, style = Stroke(
                            width = path.strokeWidth,
                            cap = path.cap,
                            join = StrokeJoin.Round,
                            pathEffect = path.style
                        ), alpha = path.alpha
                    )
                }
            }
        }) {
        if (isTouchEventActive.value && activeTool.value == Tools.ColorPicker) {
            ColorPickerTool(canvasController = canvasController, selectedPosition.value)
        } else if (isTouchEventActive.value && activeTool.value == Tools.Eraser) {
            Eraser(selectedPosition = selectedPosition, canvasController.eraserWidth)
        }
    }
}
