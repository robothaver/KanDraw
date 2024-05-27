package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.Tools

@Composable
fun Tools(
    activeTool: MutableState<Tools>,
    canvasController: CanvasController,
    selectedDialog: MutableState<Dialogs>,
    size: Dp,
    padding: Dp
) {
    PenItem(
        activeTool = activeTool.value,
        tint = canvasController.penSettings.value.penColor.color,
        size = size,
        padding = padding
    ) {
        if (activeTool.value == Tools.Pen || activeTool.value == Tools.ColorPicker) {
            selectedDialog.value = Dialogs.PenSettings
            activeTool.value = Tools.Pen
        } else {
            activeTool.value = Tools.Pen
        }
    }
    ToolBarActionItem(
        activeTool = activeTool.value,
        toolName = Tools.Eraser,
        size = size,
        icon = painterResource(id = R.drawable.eraser_solid),
        padding = padding
    ) {
        if (selectedDialog.value == Dialogs.None) {
            if (activeTool.value == Tools.Eraser) {
                selectedDialog.value = Dialogs.EraserSettings
            }
            activeTool.value = Tools.Eraser
        }
    }

    ToolBarActionItem(
        activeTool = activeTool.value,
        toolName = Tools.Mover,
        size = size,
        icon = painterResource(id = R.drawable.mover),
        padding = padding
    ) {
        if (selectedDialog.value == Dialogs.None) {
            activeTool.value = Tools.Mover
        }
    }
}