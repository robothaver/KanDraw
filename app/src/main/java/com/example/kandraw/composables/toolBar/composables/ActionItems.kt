package com.example.kandraw.composables.toolBar.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import com.example.kandraw.R
import com.example.kandraw.domain.canvasController.CanvasController
import com.example.kandraw.viewModel.Tools

@Composable
fun ToolbarActionItems(
    activeTool: MutableState<Tools>,
    isDialogVisible: MutableState<Boolean>,
    canvasController: CanvasController
) {
    PenItem(
        activeTool = activeTool,
        isDialogVisible = isDialogVisible,
        canvasController = canvasController
    )
    ToolBarActionItem(
        activeTool = activeTool,
        toolName = Tools.Eraser,
        painterResource(id = R.drawable.eraser_solid)
    )

    ToolBarActionItem(
        activeTool = activeTool,
        toolName = Tools.Mover,
        painterResource(id = R.drawable.mover)
    )
}