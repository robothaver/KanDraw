package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController

@Composable
fun OptionButtons(
    canvasController: CanvasController,
    selectedDialog: MutableState<Dialogs>,
    size: Dp,
    padding: Dp
) {
    val undo = remember { { canvasController.undo() } }
    val redo = remember { { canvasController.redo() } }
    ToolBarItem(
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        size = size,
        enabled = canvasController.undoPaths.isNotEmpty(),
        onClick = undo,
        padding = padding
    )
    ToolBarItem(
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        size = size,
        enabled = canvasController.redoPaths.isNotEmpty(),
        onClick = redo,
        padding = padding
    )
    ToolBarItem(
        icon = Icons.Filled.Menu,
        size = size,
        padding = padding
    ) {
        selectedDialog.value = Dialogs.Preferences
    }
}