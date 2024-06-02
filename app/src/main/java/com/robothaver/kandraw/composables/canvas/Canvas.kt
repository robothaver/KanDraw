package com.robothaver.kandraw.composables.canvas

import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.pointerInput
import com.robothaver.kandraw.composables.canvas.canvasCore.CanvasDrawer
import com.robothaver.kandraw.composables.canvas.canvasCore.CanvasEventHandler
import com.robothaver.kandraw.composables.canvas.canvasCore.detectTouchEvent
import com.robothaver.kandraw.composables.canvas.tools.ColorPickerTool
import com.robothaver.kandraw.composables.canvas.tools.Eraser
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.Tools
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Canvas(
    activeTool: MutableState<Tools>,
    viewPortPosition: MutableState<Offset>,
    canvasController: CanvasController,
    controller: CaptureController,
) {
    val gridSettings = canvasController.gridSettings.value
    val image = canvasController.backgroundImage.value
    val selectedPosition = remember { mutableStateOf(Offset(0f, 0f)) }
    val canvasEventHandler = CanvasEventHandler(
        selectedPosition,
        canvasController.touchActive,
        activeTool,
        canvasController,
        viewPortPosition
    )
    Box(modifier = Modifier
        .background(canvasController.backgroundColor.value.color)
        .capturable(controller)
        .fillMaxSize()
        .clipToBounds()
        .pointerInput(Unit) {
            detectTouchEvent(canvasEventHandler)
        }
        .drawBehind {
            val canvasDrawer = CanvasDrawer(gridSettings, this, image, viewPortPosition.value)

            canvasDrawer.drawBackgroundGrid()
            canvasDrawer.drawBackgroundImage()
            canvasDrawer.drawPaths(canvasController, activeTool.value)

        }) {
        if (canvasController.touchActive.value && activeTool.value == Tools.ColorPicker) {
            ColorPickerTool(canvasController, selectedPosition.value)
        } else if (canvasController.touchActive.value && activeTool.value == Tools.Eraser) {
            Eraser(selectedPosition, canvasController.eraserWidth)
        }
    }
}