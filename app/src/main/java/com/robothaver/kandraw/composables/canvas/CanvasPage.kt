package com.robothaver.kandraw.composables.canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.composables.toolBar.ToolBar
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.CanvasViewModel
import dev.shreyaspatil.capturable.controller.CaptureController

@Composable
fun CanvasPage(viewModel: CanvasViewModel, canvasController: CanvasController, captureController: CaptureController) {
    val containerSize = remember { mutableStateOf(IntSize(0, 0)) }

    // Resize background image when screen is rotated or settings are changed
    LaunchedEffect(key1 = viewModel.backgroundImage.value.scaleMode) {
        canvasController.canvasSize.value = containerSize.value
        if (viewModel.backgroundImage.value.image != null) {
            canvasController.resizeBitmap()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned {
            if (containerSize.value.width == 0) {
                containerSize.value = it.size
            }
        }
    ) {
        Canvas(
            viewModel.activeTool,
            viewModel.viewportPosition,
            canvasController,
            captureController,
        )
        ToolBar(
            canvasController,
            viewModel.activeTool,
            viewModel.selectedDialog,
            containerSize,
            viewModel.undoPaths.isNotEmpty(),
            viewModel.redoPaths.isNotEmpty()
        )
    }
}