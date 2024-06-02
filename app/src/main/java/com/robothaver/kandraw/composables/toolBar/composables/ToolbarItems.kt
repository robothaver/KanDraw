package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.ToolbarSize
import com.robothaver.kandraw.viewModel.data.Tools

@Composable
fun ToolbarItems(
    activeTool: MutableState<Tools>,
    canvasController: CanvasController,
    selectedDialog: MutableState<Dialogs>,
    toolbarSize: ToolbarSize
) {
    val size = when (toolbarSize) {
        ToolbarSize.Small -> 32.dp
        ToolbarSize.Medium -> 46.dp
        ToolbarSize.Large -> 52.dp
    }
    val padding = when (toolbarSize) {
        ToolbarSize.Small -> 3.dp
        ToolbarSize.Medium -> 6.dp
        ToolbarSize.Large -> 6.dp
    }
    Tools(activeTool, canvasController, selectedDialog, size, padding)
    OptionButtons(canvasController, selectedDialog, size, padding)
}